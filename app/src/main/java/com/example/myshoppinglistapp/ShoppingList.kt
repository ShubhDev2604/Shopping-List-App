package com.example.myshoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp() {

    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    /* It has made a mutable list of ShoppingItem dataclass named sItems,
    so that any change in the shopping list can be reflected on the screen.*/

    var showDialog by remember { mutableStateOf(false) } //If it is true it will show the additem box, else it will not.
    var itemName by remember { mutableStateOf("") } //stores item name
    var itemQuantity by remember { mutableStateOf("") } //stores item quantity
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item!!")
        }
        LazyColumn(
            /*
            Importance of this lazy column,Even if we have like 100 or 1000 items in the list, the items that are visible on the screen
            (plus 1-1 item up and down) will be editable other items will be available to be edited when we scroll up and down.
             */
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) //Pushes button to top as
            // Size of screen is fully occupied by LazyColumn()
        ) {
            items(sItems) {      //Code that is saving the edited item to the list //items is a function under lazycolumn
                    item ->  //instead of using it, we are using item-> to focus on that item we are working on.
                if (item.isEditing) { //if isEditing is true, we will open the ShoppingItemEditor composable.
                    ShoppingItemEditor(item = item, onEditComplete = { editedName, editedQuantity ->
                        sItems = sItems.map { it.copy(isEditing = false) }  //agar ye nahi hua toh editing wali window band nahi hogi and abhi tak lisst mei edited item store nahi hua hai.
                        val editedItem = sItems.find { it.id == item.id } //80-84 agar ye nahi hota toh edited item save na hota to the list aur list mei kuch change nahi hota
                        editedItem?.let {  //here it is local keyword for this code snippet
                            /*
                            Here it will search the list one by one and when the it.id matches with item.id which contains the edited
                            name and editedQuantity, these later things will be stored in it.name and it.quantity.This is basically editing the list
                             */
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                        /*
                        item.name = editedName   edited item wont be copied to list like this.
                        item.quantity = editedQuantity
                        */
                    })
                } else {  //else the shopping list will be displayed  by ShoppingList
                    ShoppingList(
                        item = item,
                        onEditClick = {
                        //finding out which item we are editing
                        sItems =
                            sItems.map { it.copy(isEditing = it.id == item.id) } //if the id matches, editing option for that id will open.
                    },
                        onDeleteClick = {
                        sItems = sItems-item  //line that deletes the item from the list.
                    })
                }
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },  //Pop-up will fade away when you click somewhere else
                confirmButton = {  /*Similar to Text since it is accepting a composable instead of providing one
                                button we are using row composable to add two buttons. */
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween /*Items next to each other but
                                also pushed away from each other so that there is proper space in between them.*/
                    ) {
                        Button(onClick = {
                            if (itemName.isNotBlank() && itemQuantity.isNotBlank()) {  //if these two items are not blank then it will turn out to be true.
                                val newItem = ShoppingItem( //making a new ShoppingItem item
                                    id = sItems.size + 1,  //updating id to be +1 of size() of sItems list.
                                    /* If the list has two items then id will be stored as 3. */
                                    name = itemName,  //storing itemName as name
                                    quantity = itemQuantity.toInt() //storing quantity as Int
                                )
                                sItems =
                                    sItems + newItem //Adding this newITem to our ShoppingList list
                                showDialog = false //Closing the DialogBox
                                itemName = ""    /* the TextField so that when we open the box again,
                                                  we wont see the last added item inside the box */
                                itemQuantity = "" //Similar to itemName Above
                            }
                        }) {
                            Text("Add")
                        }
                        Button(onClick = { showDialog = false }) {
                            Text("Cancel")  // Pop-up will fade away when you click on the button
                        }
                    }
                },
                title = { Text("Add Shopping Item") }, // title of pop-up
                text = {
                    /*
                Since text is a composable and expect a composable inside it so instead of providing it a Text
                Composable we are adding Column composable which is technically allowed but not what text comp
                -sable was made for.
                 */
                    Column {
                        OutlinedTextField(
                            value = itemName, //for what value TextField is
                            onValueChange = {
                                itemName = it
                            }, //it stores what's being written in TextField in itemName.
                            label = { Text("Item Name:") }, //label provides a label for the box.
                            singleLine = true,  //only single line is provided to add a item.. no paras
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp) //to widden the textField and provides padding
                            // of 8.dp
                        )

                        OutlinedTextField(
                            /*
                        Similar to itemName TextOutfield
                         */
                            value = itemQuantity,
                            onValueChange = { itemQuantity = it },
                            label = { Text("Item Quantity:") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            )
        }
    }
}

    @Composable
            /*
FUnction to edit the already entered item in the list.
 */
    fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String, Int) -> Unit) {
        var editedName by remember { mutableStateOf(item.name) }
        var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
        var isEditing by remember { mutableStateOf(item.isEditing) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                BasicTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    singleLine = true,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp)
                )
                BasicTextField(
                    value = editedQuantity,
                    onValueChange = { editedQuantity = it },
                    singleLine = true,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp)
                )
            }

            Button(
                onClick = {
                    isEditing = false
                    onEditComplete(
                        editedName,
                        editedQuantity.toIntOrNull() ?: 1
                    ) //passes either int or 1 instead of gibberish text and null
                }
            ) {
                Text("Save")
            }
        }

    }

    @Composable
    fun ShoppingList(
        item: ShoppingItem,
        onEditClick: () -> Unit, //This is a lambda function and it gets executed when the edit action is triggered.
        // Doesn't take any parameter and doesn't return anything
        //This is just like onClick which happens to be a lambda function itself, Here We're making our own onClick Function
        //It's like passing a function
        //onClicking the button, that snippet of code will run
        onDeleteClick: () -> Unit,
    ) {
        Row(  /*Will print every detail inside the box ek baari mei jo bhi display ho raha hai vo sab ek hi row mei
            And because of LazyColumn we are able to see every item in separate row */
            modifier = Modifier
                .padding(8.dp) //makes a padding of 8.dp around the box
                .fillMaxWidth() //make the row or box to fill the width of the screen
                .border(
                    border = BorderStroke(
                        2.dp,
                        Color(0XFF018786)
                    ), // It will create one single border around everything the row shows
                    shape = RoundedCornerShape(20) //creates rounded border
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item.name, modifier = Modifier.padding(8.dp))
            Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
            Row(modifier = Modifier.padding(8.dp)) {
                IconButton(onClick = onEditClick) {  //to provide a clickable icon
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        }
    }


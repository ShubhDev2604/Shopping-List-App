package com.example.myshoppinglistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp(){

    var sItems by remember{ mutableStateOf(listOf<ShoppingItem>()) }
    /* It has made a mutable list of ShoppingItem dataclass named sItems,
    so that any change in the shopping list can be reflected on the screen.*/

    var showDialog by remember { mutableStateOf(false) } //If it is true it will show the additem box, else it will not.
    var itemName by remember { mutableStateOf("") } //stores item name
    var itemQuantity by remember { mutableStateOf("") } //stores item quantity
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item!!")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) //Pushes button to top as
            // Size of screen is fully occupied by LazyColumn()
        ){
            items(sItems){

            }
        }
    }

    if(showDialog){
        AlertDialog(
            onDismissRequest = {showDialog = false },  //Pop-up will fade away when you click somewhere else
            confirmButton = {  /*Similar to Text since it is accepting a composable instead of providing one
                                button we are using row composable to add two buttons. */
                            Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween /*Items next to each other but
                                also pushed away from each other so that there is proper space in between them.*/
                            ){
                                Button(onClick = {
                                    if(itemName.isNotBlank() && itemQuantity.isNotBlank()){  //if these two items are not blank then it will turn out to be true.
                                        val newItem = ShoppingItem( //making a new ShoppingItem item
                                            id = sItems.size+1,  //updating id to be +1 of size() of sItems list.
                                            /* If the list has two items then id will be stored as 3. */
                                            name = itemName,  //storing itemName as name
                                            quantity = itemQuantity.toInt() //storing quantity as Int
                                        )
                                        sItems = sItems + newItem //Adding this newITem to our ShoppingList list
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
            title = { Text("Add Shopping Item")}, // title of pop-up
            text = {
                /*
                Since text is a composable and expect a composable inside it so instead of providing it a Text
                Composable we are adding Column composable which is technically allowed but not what text comp
                -sable was made for.
                 */
                Column {
                    OutlinedTextField(
                        value = itemName, //for what value TextField is
                        onValueChange = { itemName = it }, //it stores what's being written in TextField in itemName.
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
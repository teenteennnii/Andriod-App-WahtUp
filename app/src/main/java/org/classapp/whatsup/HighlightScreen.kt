package org.classapp.whatsup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import org.classapp.whatsup.ui.theme.WhatsUpTheme
import java.text.SimpleDateFormat

private fun getEventsFromFirebase(onSuccess: (QuerySnapshot)-> Unit,
                                  onFailure: (Exception)->Unit){
    val db = Firebase.firestore
    db.collection("events").get()
        .addOnSuccessListener { result -> onSuccess(result)}
        .addOnFailureListener {exception -> onFailure(exception)}
}

data class Event(
    val event_name:String? = "",
    val event_venue:String? = "",
    val start_date: Timestamp? = null,
    val end_date:Timestamp? = null
)

@Composable
fun HighlightScreen() {
    val screenContext = LocalContext.current
    val eventList = remember {mutableStateListOf<Event?>()}
    val onFilebaseQueryFailed = {
        e:Exception -> Toast.makeText(screenContext, e.message, Toast.LENGTH_LONG).show()
    }
    val onFilebaseQuerySuccess = {result:QuerySnapshot ->
        if (!result.isEmpty) {
            val resultDocuments = result.documents
            for (document in resultDocuments) {
                val event:Event? = document.toObject(Event::class.java)
                eventList.add(event)

                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val start = dateFormatter.format(event?.start_date?.toDate())
                val end = dateFormatter.format(event?.end_date?.toDate())
                Toast.makeText(screenContext, "${event?.event_name} at ${event?.event_venue} ($start - $end)",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
    getEventsFromFirebase(onFilebaseQuerySuccess, onFilebaseQueryFailed)

    WhatsUpTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column ( modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                Text(text = "Highlights")
                EventList(events = eventList)
            }
        }
    }
}

@Composable
fun EventItem(event: Event){
    ElevatedCard (elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.md_theme_background))){
        Column (modifier = Modifier
            .fillMaxWidth(1f)
            .padding(8.dp)){
            Text(text = event.event_name!!,
                style = TextStyle(color = colorResource(id = R.color.md_theme_primary),
                fontSize = 20.sp))
            Text(text = event.event_venue!!,
                style = TextStyle(color = colorResource(id = R.color.md_theme_secondary),
                fontSize = 18.sp))
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            Row {
                Text(text = "Form: ",
                    style = TextStyle(color = colorResource(id = R.color.md_theme_secondary),
                        fontSize = 18.sp)
                )
                Text(text = dateFormatter.format(event?.start_date?.toDate()),
                    style = TextStyle(color = colorResource(id = R.color.md_theme_secondary),
                        fontSize = 18.sp)
                )
            }
            Row {
                Text(text = "To: ",
                    style = TextStyle(color = colorResource(id = R.color.md_theme_secondary),
                        fontSize = 18.sp)
                )
                Text(text = dateFormatter.format(event?.end_date?.toDate()),
                    style = TextStyle(color = colorResource(id = R.color.md_theme_secondary),
                        fontSize = 18.sp)
                )
            }
        }
    }
}

@Composable
fun EventList(events: List<Event?>){
    LazyColumn(contentPadding = PaddingValues(all = 4.dp)) {
        items(items = events.filterNotNull()) {
            EventItem(event = it)
        }
    }
}

package org.karczelapp.withinrange

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.karczelapp.withinrange.ui.theme.WithinRangeTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase as ktxFirebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage as ktxStorage
import com.google.firebase.storage.storage
import org.karczelapp.withinrange.dataclass.User
import java.util.UUID

fun addUserIfNotExist() {
    var auth: FirebaseAuth = Firebase.auth
    val db = Firebase.firestore
    val uid = auth.currentUser?.uid ?: ""

    db.collection("users")
        .whereEqualTo("uid", uid)
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                // Not found, add new user
                val user = hashMapOf(
                    "uid" to uid,
                    "name" to "Your Default Name",
                    "groupIds" to listOf<String>(),
                    "profilePicUrl" to null
                )
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Firestore", "User added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error adding user", e)
                    }
            } else {
                Log.d("Firestore", "User with uid $uid already exists")
            }
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error checking user existence", e)
        }
}

@Composable
fun ProfileScreen() {

    val context = LocalContext.current
    var isUploading by remember { mutableStateOf(false) }

    var auth: FirebaseAuth = Firebase.auth
    val user = auth.currentUser

    var userData by remember { mutableStateOf<User?>(null) }
    var displayName by remember { mutableStateOf("") }

    // Load from Firestore once into User
    LaunchedEffect(user?.uid) {

        addUserIfNotExist()

        user?.uid?.let { uid ->
            Firebase.firestore.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        Log.d("load_user_info", "${doc.id} => ${doc.data}")

                        val fetchedUser = User(
                            name = doc.getString("name") ?: "",
                            groupIds = doc.get("groupIds") as? List<String> ?: listOf(),
                            profilePicUrl = doc.getString("profilePicUrl"),
                        )
                        userData = fetchedUser
                        displayName = fetchedUser.name ?: ""
                    } else {
                        Log.d("load_user_info", "No such user document")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("load_user_info", "Failed to get user document", e)
                }
        }
    }

    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            uri?.let {
                isUploading = true
                user?.uid?.let { uid ->
                    uploadProfilePic(context, user.uid, it) { downloadUrl ->
                        isUploading = false
                        if (downloadUrl != null) {
                            userData = userData?.copy(profilePicUrl = downloadUrl)
                            Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile image (or placeholder)
            if (userData?.profilePicUrl != null && userData?.profilePicUrl != "") {
                Image(
                    painter = rememberAsyncImagePainter(userData!!.profilePicUrl),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(userData?.color ?: colorResource(R.color.green))
                )
            }

            // Name (Editable)
            OutlinedTextField(
                value = displayName,
                onValueChange = { newName ->
                    displayName = newName
                },
                label = { Text("Display Name") }
            )

            // Save display name Button (update to Firestore)
            Button(onClick = {
                userData?.let { thisUser ->
                    val updatedUser = thisUser.copy(name = displayName)
                    Firebase.firestore.collection("users").document(user!!.uid)
                        .set(updatedUser)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Name updated", Toast.LENGTH_SHORT).show()
                            userData = updatedUser
                            displayName = updatedUser.name!!
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to update name", Toast.LENGTH_SHORT).show()
                        }
                }
            }) {
                Text("Save Name")
            }

            // Email
            Text(
                text = user?.email ?: "you@example.com",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

//            // Change profile image Button
//            Button(
//                onClick = {
//                    // Trigger the image picker
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//                        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//                    } else {
//                        // For older Android versions, continue with the previous method
////                        launcher.launch("image/*")
//                    }
//                },
//                enabled = !isUploading
//            ) {
//                Text(if (isUploading) "Uploading..." else "Edit Profile Picture")
//            }
        }
    }
}

// Payment required for firebase storage; skip
fun uploadProfilePic(context: Context, uid: String, imageUri: Uri, onComplete: (String?) -> Unit) {
    val storageReference = FirebaseStorage.getInstance().reference

    // Ensure imageUri is not null
    if (imageUri != null) {
        // Create a unique path in Firebase Storage
        val ref = storageReference.child("images/${UUID.randomUUID()}")

        // Upload the file to Firebase Storage
        ref.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { downloadUrl ->
                    // Call onComplete with the download URL
                    onComplete(downloadUrl.toString())
                }
            }
            .addOnFailureListener { e ->
                // Show a failure message
                Toast.makeText(context, "Upload Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                onComplete(null)
            }
            .addOnProgressListener { taskSnapshot ->
                // Handle progress updates (if needed)
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                // You can show progress on the UI (e.g., with a CircularProgressIndicator)
            }
    } else {
        // Handle case where no image is selected
        Toast.makeText(context, "No image selected!", Toast.LENGTH_SHORT).show()
        onComplete(null)
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    WithinRangeTheme {
        ProfileScreen()
    }
}
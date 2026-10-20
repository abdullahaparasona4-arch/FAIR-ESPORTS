package com.fairesports.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF0A1020)
private val Panel = Color(0xFF151F33)
private val Blue = Color(0xFF2878FF)
private val TextWhite = Color(0xFFF4F7FF)
private val Muted = Color(0xFF9AA8C1)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FaireSportsApp() }
    }
}

data class Tournament(
    val title: String,
    val game: String,
    val date: String,
    val entryFee: String,
    val prize: String,
    val status: String
)

private val demoTournaments = listOf(
    Tournament("Friday Night Cup", "Free Fire", "শুক্রবার • ৮:৩০ PM", "৳২০", "৳১,০০০", "রেজিস্ট্রেশন চলছে"),
    Tournament("FAIRE Open Challenge", "Free Fire", "শনিবার • ৯:০০ PM", "৳৩০", "৳২,০০০", "রেজিস্ট্রেশন চলছে"),
    Tournament("Community Showdown", "E-sports", "শিগগিরই", "ফ্রি", "৳৫০০", "আসন্ন")
)

@Composable
fun FaireSportsApp() {
    var selectedTab by remember { mutableStateOf("হোম") }
    var showAuth by remember { mutableStateOf(false) }
    var authMode by remember { mutableStateOf("লগইন") }
    var selectedTournament by remember { mutableStateOf<Tournament?>(null) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Blue,
            background = Navy,
            surface = Panel,
            onBackground = TextWhite,
            onSurface = TextWhite
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = Navy) {
            if (showAuth) {
                AuthScreen(
                    mode = authMode,
                    onModeChange = { authMode = it },
                    onBack = { showAuth = false },
                    onSubmit = { /* Firebase Auth integration is wired in the next implementation step */ }
                )
            } else if (selectedTournament != null) {
                TournamentDetails(
                    tournament = selectedTournament!!,
                    onBack = { selectedTournament = null },
                    onRegister = { showAuth = true; authMode = "রেজিস্ট্রেশন" }
                )
            } else {
                Scaffold(
                    containerColor = Navy,
                    bottomBar = {
                        NavigationBar(containerColor = Panel, contentColor = TextWhite) {
                            listOf(
                                "হোম" to Icons.Default.Home,
                                "ম্যাচ" to Icons.Default.SportsEsports,
                                "নোটিফিকেশন" to Icons.Default.Notifications,
                                "প্রোফাইল" to Icons.Default.Person
                            ).forEach { (label, icon) ->
                                NavigationBarItem(
                                    selected = selectedTab == label,
                                    onClick = {
                                        selectedTab = label
                                        if (label == "প্রোফাইল") { showAuth = true; authMode = "লগইন" }
                                    },
                                    icon = { Icon(icon, contentDescription = label) },
                                    label = { Text(label) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Blue,
                                        indicatorColor = Color(0xFF20365C),
                                        unselectedIconColor = Muted,
                                        selectedTextColor = TextWhite,
                                        unselectedTextColor = Muted
                                    )
                                )
                            }
                        }
                    }
                ) { padding ->
                    when (selectedTab) {
                        "ম্যাচ" -> MatchScreen(Modifier.padding(padding))
                        "নোটিফিকেশন" -> NotificationsScreen(Modifier.padding(padding))
                        else -> HomeScreen(
                            modifier = Modifier.padding(padding),
                            onLogin = { showAuth = true; authMode = "লগইন" },
                            onTournament = { selectedTournament = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier, onLogin: () -> Unit, onTournament: (Tournament) -> Unit) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("FAIRE", color = TextWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)
                    Text("SPORTS ARENA", color = Blue, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
                }
                FilledTonalButton(onClick = onLogin) { Icon(Icons.Default.Person, null); Spacer(Modifier.width(6.dp)); Text("লগইন") }
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth().background(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(listOf(Color(0xFF173F91), Color(0xFF10192B))),
                    shape = RoundedCornerShape(24.dp)
                ).padding(22.dp)
            ) {
                Text("PLAY. COMPETE. WIN.", color = TextWhite, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(8.dp))
                Text("তোমার পরের ম্যাচের জন্য প্রস্তুত হও।", color = Color(0xFFD5E2FF))
                Spacer(Modifier.height(18.dp))
                Button(onClick = { onTournament(demoTournaments.first()) }, colors = ButtonDefaults.buttonColors(containerColor = Blue)) {
                    Text("টুর্নামেন্ট দেখো"); Spacer(Modifier.width(8.dp)); Icon(Icons.Default.ArrowForward, null)
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("চলমান/আসন্ন টুর্নামেন্ট", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Text("সব দেখুন", color = Blue, fontSize = 13.sp)
            }
        }
        items(demoTournaments) { tournament ->
            TournamentCard(tournament = tournament, onClick = { onTournament(tournament) })
        }
        item {
            Text("প্রথম সংস্করণ • ডেমো ডেটা", color = Muted, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun TournamentCard(tournament: Tournament, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(tournament.title, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text(tournament.game, color = Muted, fontSize = 13.sp)
                }
                Surface(color = Color(0xFF173D36), shape = RoundedCornerShape(50)) {
                    Text(tournament.status, color = Color(0xFF7CE5BC), fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                }
            }
            HorizontalDivider(color = Color(0xFF29364D))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                InfoColumn("এন্ট্রি ফি", tournament.entryFee)
                InfoColumn("প্রাইজ পুল", tournament.prize)
                InfoColumn("সময়", tournament.date)
            }
        }
    }
}

@Composable
fun InfoColumn(label: String, value: String) {
    Column(Modifier.widthIn(max = 130.dp)) {
        Text(label, color = Muted, fontSize = 11.sp)
        Text(value, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}

@Composable
fun TournamentDetails(tournament: Tournament, onBack: () -> Unit, onRegister: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        TextButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null); Text("ফিরে যাও") }
        Spacer(Modifier.height(20.dp))
        Text(tournament.title, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Text(tournament.game, color = Blue)
        Spacer(Modifier.height(24.dp))
        TournamentCard(tournament) {}
        Spacer(Modifier.height(18.dp))
        Text("টুর্নামেন্টে অংশ নিতে প্লেয়ার রেজিস্ট্রেশন প্রয়োজন।", color = Muted)
        Spacer(Modifier.weight(1f))
        Button(onClick = onRegister, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Blue)) {
            Text("রেজিস্ট্রেশন করুন")
        }
    }
}

@Composable
fun MatchScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("ম্যাচ ও ফলাফল", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Text("এখানে লাইভ স্কোর ও ম্যাচের ফলাফল যুক্ত হবে।", color = Muted)
        Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.SportsEsports, null, tint = Blue, modifier = Modifier.size(32.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("FAIRESPORTS Match Center", fontWeight = FontWeight.Bold)
                    Text("লাইভ ডেটা সংযুক্ত করা হয়নি", color = Muted, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun NotificationsScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("নোটিফিকেশন", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.NotificationsActive, null, tint = Blue, modifier = Modifier.size(30.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("নতুন আপডেট এখানে দেখাবে")
                    Text("Firebase Cloud Messaging সংযুক্ত করার পর নোটিফিকেশন আসবে।", color = Muted, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AuthScreen(mode: String, onModeChange: (String) -> Unit, onBack: () -> Unit, onSubmit: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        TextButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null); Text("ফিরে যাও") }
        Text(if (mode == "লগইন") "স্বাগতম" else "অ্যাকাউন্ট তৈরি", fontSize = 29.sp, fontWeight = FontWeight.ExtraBold)
        Text("FAIRESPORTS অ্যাকাউন্ট", color = Muted)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = mode == "লগইন", onClick = { onModeChange("লগইন") }, label = { Text("লগইন") })
            FilterChip(selected = mode == "রেজিস্ট্রেশন", onClick = { onModeChange("রেজিস্ট্রেশন") }, label = { Text("রেজিস্ট্রেশন") })
        }
        if (mode == "রেজিস্ট্রেশন") OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("পূর্ণ নাম") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("ইমেইল") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("পাসওয়ার্ড") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Button(onClick = onSubmit, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Blue)) {
            Text(if (mode == "লগইন") "লগইন করুন" else "অ্যাকাউন্ট তৈরি করুন")
        }
        Text("নোট: Firebase Authentication এখনো কনফিগার করা হয়নি।", color = Muted, fontSize = 12.sp)
    }
}

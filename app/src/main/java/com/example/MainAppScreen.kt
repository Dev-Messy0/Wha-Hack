package com.example

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.WebMessengerTheme
import com.example.ui.theme.WhatsAppDarkTeal
import com.example.ui.theme.WhatsAppGreen

const val WHATSAPP_WEB_URL = "https://web.whatsapp.com/"
const val DESKTOP_USER_AGENT =
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36"
const val MOBILE_USER_AGENT =
    "Mozilla/5.0 (Linux; Android 14; Pixel 8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.6261.119 Mobile Safari/537.36"

data class GuideLanguage(
    val code: String,
    val name: String,
    val flag: String,
    val title: String,
    val subtitle: String,
    val creatorTag: String,
    val steps: List<String>,
    val startButtonText: String,
    val noteText: String
)

val GUIDE_LANGUAGES = listOf(
    GuideLanguage(
        code = "FR",
        name = "Français",
        flag = "🇫🇷",
        title = "INTERFACE DE CONNEXION HACKER",
        subtitle = "Guide de synchronisation d'appareil",
        creatorTag = "Créé par Dev Messy (But Éducatif)",
        steps = listOf(
            "Prenez le téléphone de la cible ou ouvrez son application WhatsApp.",
            "Accédez aux Réglages / Menu (3 points) > Appareils connectés.",
            "Appuyez sur le bouton 'Connecter un appareil'.",
            "Scannez le QR Code terminal qui s'affiche sur cette interface.",
            "La session est synchronisée et interceptée en mode Bureau."
        ),
        startButtonText = "CONTINUER ET DÉMARRER LA SESSION",
        noteText = "⚠️ AVERTISSEMENT : Cette application est créée à des fins strictement éducatives. Dev Messy décline toute responsabilité en cas de non-respect des règles d'utilisation de WhatsApp."
    ),
    GuideLanguage(
        code = "EN",
        name = "English",
        flag = "🇬🇧",
        title = "HACKER CONNECTION INTERFACE",
        subtitle = "Device Synchronization Guide",
        creatorTag = "Created by Dev Messy (Educational)",
        steps = listOf(
            "Open the target's phone or launch their WhatsApp app.",
            "Navigate to Settings / Menu (3 dots) > Linked Devices.",
            "Tap on 'Link a Device'.",
            "Scan the terminal QR Code displayed on this screen.",
            "The session is synchronized and active in Desktop mode."
        ),
        startButtonText = "CONTINUE AND START SESSION",
        noteText = "⚠️ DISCLAIMER: This app is created for educational purposes only. Dev Messy is not responsible for any misuse or non-compliance with WhatsApp terms of service."
    ),
    GuideLanguage(
        code = "ZH",
        name = "中文",
        flag = "🇨🇳",
        title = "黑客连接界面",
        subtitle = "设备同步指南",
        creatorTag = "Dev Messy 制作 (教育用途)",
        steps = listOf(
            "打开目标手机上的 WhatsApp 应用程序。",
            "进入 设置 / 菜单（3个点）> 已关联的设备。",
            "点击 “关联设备” 按钮。",
            "扫描此终端屏幕上显示的二维码。",
            "会话已在桌面模式下同步完成。"
        ),
        startButtonText = "继续并启动会话",
        noteText = "⚠️ 免责声明：本应用仅供教育目的使用。对于违反 WhatsApp 使用规则或法律的行为，Dev Messy 概不负责。"
    ),
    GuideLanguage(
        code = "HI",
        name = "हिन्दी",
        flag = "🇮🇳",
        title = "हैकर कनेक्शन इंटरफ़ेस",
        subtitle = "डिवाइस सिंक्रोनाइज़ेशन गाइड",
        creatorTag = "Dev Messy द्वारा निर्मित (शैक्षणिक)",
        steps = listOf(
            "लक्ष्य फोन पर व्हाट्सएप ऐप खोलें।",
            "सेटिंग्स / मेनू (3 बिंदु) > 'लिंक किए गए डिवाइस' पर जाएं।",
            "'डिवाइस लिंक करें' बटन पर टैप करें।",
            "इस स्क्रीन पर प्रदर्शित टर्मिनल QR कोड को स्कैन करें।",
            "सत्र डेस्कटॉप मोड में सिंक हो गया है।"
        ),
        startButtonText = "जारी रखें और सत्र शुरू करें",
        noteText = "⚠️ अस्वीकरण: यह ऐप केवल शैक्षणिक उद्देश्यों के लिए बनाया गया है। व्हाट्सएप के नियमों के उल्लंघन के लिए Dev Messy जिम्मेदार नहीं है।"
    ),
    GuideLanguage(
        code = "UR",
        name = "اردو",
        flag = "🇵🇰",
        title = "ہیکر کنیکشن انٹرفیس",
        subtitle = "ڈیوائس مطابقت پذیری گائیڈ",
        creatorTag = "Dev Messy کی طرف سے بنایا گیا (تعلیمی)",
        steps = listOf(
            "ٹارگٹ فون پر واٹس ایپ ایپ کھولیں۔",
            "سیٹنگز / مینو (3 نقطے) > 'منسلک ڈیوائسز' پر جائیں۔",
            "'ڈیوائس منسلک کریں' کے بٹن پر ٹیپ کریں۔",
            "اس اسکرین پر دکھائے جانے والے ٹرمینل QR کوڈ کو اسکین کریں۔",
            "سیشن ڈیسک ٹاپ موڈ میں مطابقت پذیر ہو گیا ہے۔"
        ),
        startButtonText = "جاری رکھیں اور سیشن شروع کریں",
        noteText = "⚠️ ڈس کلیمر: یہ ایپ صرف تعلیمی مقاصد کے لیے بنائی گئی ہے۔ واٹس ایپ کی شرائط کی خلاف ورزی کے لیے Dev Messy ذمہ دار نہیں ہے۔"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen() {
    val context = LocalContext.current
    var showOnboarding by remember { mutableStateOf(true) }
    var selectedLangCode by remember { mutableStateOf("FR") }

    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var loadingProgress by remember { mutableIntStateOf(0) }
    var isDesktopMode by remember { mutableStateOf(true) }
    var showDirectChatDialog by remember { mutableStateOf(false) }
    var showInstructionsDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permission caméra accordée pour le scan QR", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "La caméra est requise pour scanner le QR Code", Toast.LENGTH_LONG).show()
        }
    }

    if (showOnboarding) {
        HackerOnboardingScreen(
            selectedLangCode = selectedLangCode,
            onLanguageSelected = { selectedLangCode = it },
            onStartSession = { showOnboarding = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF00FF66).copy(alpha = 0.15f),
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(end = 4.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "☠",
                                        fontSize = 18.sp,
                                        color = Color(0xFF00FF66)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Wha Hack",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00FF66)
                                )
                                Text(
                                    text = "SESSION SECURE v2.0 • Dev Messy",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0D1117),
                        titleContentColor = Color.White,
                        actionIconContentColor = Color(0xFF00FF66)
                    ),
                    actions = {
                        IconButton(
                            onClick = { showOnboarding = true },
                            modifier = Modifier.testTag("help_guide_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Help,
                                contentDescription = "Guide & Langues",
                                tint = Color(0xFF00FF66)
                            )
                        }

                        IconButton(
                            onClick = { showDirectChatDialog = true },
                            modifier = Modifier.testTag("direct_chat_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "Message Direct",
                                tint = Color(0xFF00FF66)
                            )
                        }

                        IconButton(
                            onClick = { webViewInstance?.reload() },
                            modifier = Modifier.testTag("refresh_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Rafraîchir",
                                tint = Color(0xFF00FF66)
                            )
                        }

                        Box {
                            IconButton(
                                onClick = { showMenu = true },
                                modifier = Modifier.testTag("menu_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Menu Options",
                                    tint = Color(0xFF00FF66)
                                )
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = if (isDesktopMode) Icons.Default.Computer else Icons.Default.Smartphone,
                                                contentDescription = null,
                                                tint = Color(0xFF00FF66),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(if (isDesktopMode) "Passer en Mobile" else "Passer en Bureau (QR)")
                                        }
                                    },
                                    onClick = {
                                        showMenu = false
                                        isDesktopMode = !isDesktopMode
                                        webViewInstance?.let { wv ->
                                            wv.settings.userAgentString =
                                                if (isDesktopMode) DESKTOP_USER_AGENT else MOBILE_USER_AGENT
                                            wv.loadUrl(WHATSAPP_WEB_URL)
                                        }
                                    }
                                )

                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.QrCodeScanner,
                                                contentDescription = null,
                                                tint = Color(0xFF00FF66),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Autoriser Caméra")
                                        }
                                    },
                                    onClick = {
                                        showMenu = false
                                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                    }
                                )

                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Info,
                                                contentDescription = null,
                                                tint = Color(0xFF00FF66),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Guide 5 Langues")
                                        }
                                    },
                                    onClick = {
                                        showMenu = false
                                        showOnboarding = true
                                    }
                                )
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFF0D1117))
            ) {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )

                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                databaseEnabled = true
                                useWideViewPort = true
                                loadWithOverviewMode = true
                                setSupportZoom(true)
                                builtInZoomControls = true
                                displayZoomControls = false
                                allowFileAccess = true
                                allowContentAccess = true
                                userAgentString = DESKTOP_USER_AGENT
                                mediaPlaybackRequiresUserGesture = false
                            }

                            val cookieManager = CookieManager.getInstance()
                            cookieManager.setAcceptCookie(true)
                            cookieManager.setAcceptThirdPartyCookies(this, true)

                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    super.onPageStarted(view, url, favicon)
                                    isLoading = true
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isLoading = false
                                }

                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?
                                ): Boolean {
                                    return false
                                }
                            }

                            webChromeClient = object : WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    loadingProgress = newProgress
                                    if (newProgress == 100) {
                                        isLoading = false
                                    }
                                }

                                override fun onPermissionRequest(request: PermissionRequest?) {
                                    request?.grant(request.resources)
                                }
                            }

                            loadUrl(WHATSAPP_WEB_URL)
                            webViewInstance = this
                        }
                    },
                    update = { wv ->
                        webViewInstance = wv
                    },
                    modifier = Modifier.fillMaxSize()
                )

                if (isLoading) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LinearProgressIndicator(
                            progress = { loadingProgress / 100f },
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF00FF66),
                            trackColor = Color(0xFF161B22)
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isLoading && loadingProgress < 40,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Surface(
                        color = Color(0xFF161B22),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.5f)),
                        tonalElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF00FF66),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Chargement de WhatsApp Web...",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "SYSTEM READY • Dev Messy",
                                color = Color(0xFF00FF66),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDirectChatDialog) {
        DirectChatModal(
            onDismiss = { showDirectChatDialog = false },
            onStartChat = { phoneNum, message ->
                showDirectChatDialog = false
                val cleanNumber = phoneNum.replace("+", "").replace(" ", "").trim()
                val encodedMsg = Uri.encode(message)
                val targetUrl = if (message.isNotBlank()) {
                    "https://web.whatsapp.com/send?phone=$cleanNumber&text=$encodedMsg"
                } else {
                    "https://web.whatsapp.com/send?phone=$cleanNumber"
                }
                webViewInstance?.loadUrl(targetUrl)
            }
        )
    }

    if (showInstructionsDialog) {
        InstructionsModal(
            onDismiss = { showInstructionsDialog = false }
        )
    }
}

@Composable
fun HackerOnboardingScreen(
    selectedLangCode: String,
    onLanguageSelected: (String) -> Unit,
    onStartSession: () -> Unit
) {
    val currentLang = GUIDE_LANGUAGES.find { it.code == selectedLangCode } ?: GUIDE_LANGUAGES.first()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0D1117)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 40.dp, bottom = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    // Header Hacker Identity
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF161B22))
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "☠",
                            fontSize = 46.sp,
                            color = Color(0xFF00FF66)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "WHA HACK v2.0",
                        color = Color(0xFF00FF66),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        color = Color(0xFF00FF66).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = currentLang.creatorTag,
                            color = Color(0xFF00FF66),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Language Selector Bar
                    Text(
                        text = "SELECT LANGUAGE / CHOISIR LA LANGUE:",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(GUIDE_LANGUAGES) { lang ->
                            val isSelected = lang.code == selectedLangCode
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Color(0xFF00FF66) else Color(0xFF161B22),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) Color(0xFF00FF66) else Color.Gray.copy(alpha = 0.4f)
                                ),
                                modifier = Modifier.clickable { onLanguageSelected(lang.code) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = lang.flag, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = lang.name,
                                        color = if (isSelected) Color.Black else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    // Guide Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                        border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.6f))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Terminal,
                                    contentDescription = null,
                                    tint = Color(0xFF00FF66),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = currentLang.title,
                                        color = Color(0xFF00FF66),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = currentLang.subtitle,
                                        color = Color.LightGray,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            currentLang.steps.forEachIndexed { index, step ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF00FF66),
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${index + 1}",
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = step,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color(0xFF0D1117),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = currentLang.noteText,
                                    color = Color(0xFF00FF66).copy(alpha = 0.9f),
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Bottom Sticky Start Button
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                color = Color(0xFF0D1117),
                tonalElevation = 12.dp,
                border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = onStartSession,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00FF66),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("start_session_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "☠ " + currentLang.startButtonText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DirectChatModal(
    onDismiss: () -> Unit,
    onStartChat: (phoneNumber: String, message: String) -> Unit
) {
    var phoneInput by remember { mutableStateOf("") }
    var messageInput by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.7f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = Color(0xFF00FF66),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Message Direct",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Fermer",
                                tint = Color.LightGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Ouvrir une discussion sans enregistrer le numéro dans vos contacts.",
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("Numéro avec indicatif (ex: 33612345678)") },
                        placeholder = { Text("33612345678") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00FF66),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color(0xFF00FF66),
                            unfocusedLabelColor = Color.LightGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("phone_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = messageInput,
                        onValueChange = { messageInput = it },
                        label = { Text("Message initial (Optionnel)") },
                        placeholder = { Text("Bonjour!") },
                        singleLine = false,
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00FF66),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color(0xFF00FF66),
                            unfocusedLabelColor = Color.LightGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("message_input")
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (phoneInput.isNotBlank()) {
                                onStartChat(phoneInput, messageInput)
                            }
                        },
                        enabled = phoneInput.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00FF66),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("start_chat_button")
                    ) {
                        Text(
                            text = "Lancer la Discussion",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InstructionsModal(
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.7f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                border = BorderStroke(1.dp, Color(0xFF00FF66).copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "☠ Wha Hack - Mode d'emploi",
                        color = Color(0xFF00FF66),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Created by Dev Messy",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "1. Ouvrez WhatsApp sur votre téléphone principal.",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "2. Allez dans Réglages ou Appareils connectés.",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "3. Appuyez sur 'Connecter un appareil'.",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "4. Scannez le QR Code affiché à l'écran.",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00FF66),
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text(text = "Compris", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

# FAIRESPORTS — Android starter project

এটি FAIRESPORTS-এর প্রথম Android source-code ভিত্তি। এটি **সম্পূর্ণ production-ready অ্যাপ নয়**; UI ও প্রাথমিক নেভিগেশন তৈরি, আর Firebase, পেমেন্ট, অ্যাডমিন workflow ও live score integration পরের ধাপে যুক্ত করতে হবে।

## প্রযুক্তি
- Kotlin + Jetpack Compose (Android UI)
- Firebase Authentication (login/registration)
- Cloud Firestore (tournament, registrations, matches, payment records)
- Firebase Cloud Messaging (push notifications)
- Payment provider integration must be done through a trusted backend

## এখন কী আছে
- বাংলা-ভিত্তিক ডার্ক স্পোর্টস UI
- হোম স্ক্রিন ও ডেমো টুর্নামেন্ট কার্ড
- টুর্নামেন্ট ডিটেইলস স্ক্রিন
- লগইন/রেজিস্ট্রেশন UI (এখনো Firebase-এ যুক্ত নয়)
- ম্যাচ ও নোটিফিকেশন placeholder
- Firestore security rules starter

## Android Studio-তে চালানো
1. Android Studio-তে `FAIRESPORTS` ফোল্ডারটি Open করো।
2. Gradle sync শেষ হতে দাও।
3. Firebase Console-এ একটি project তৈরি করো এবং Android app ID হিসেবে `com.fairesports.app` যোগ করো।
4. Firebase থেকে `google-services.json` ডাউনলোড করে `app/` ফোল্ডারে রাখো।
5. Authentication-এ Email/Password চালু করো; Firestore database তৈরি করো।
6. `firestore.rules` পরীক্ষা করে Firebase Console-এ deploy করো।
7. Run দিয়ে emulator/Android ফোনে পরীক্ষা করো।
8. APK বানাতে: **Build → Build APK(s)**।

## গুরুত্বপূর্ণ নিরাপত্তা
- `google-services.json` Firebase client configuration; কিন্তু private server secrets বা payment secret key কখনো Android app-এ রেখো না।
- Payment success client-side থেকে নিজে ঘোষণা করা যাবে না। Backend provider verification/webhook দিয়ে status update করতে হবে।
- Admin access শুধু UI-তে button লুকিয়ে নিরাপদ হয় না। Firebase custom claims বা server-verified admin role ব্যবহার করতে হবে। বর্তমান rules starter-কে production-এর আগে কঠোরভাবে পরীক্ষা করতে হবে।
- Demo tournament data hard-coded; এটি বাস্তব ম্যাচ/টুর্নামেন্টের তথ্য নয়।
- লাইভ স্কোরের জন্য নির্ভরযোগ্য data source/API দরকার; API না থাকলে admin panel থেকে ফলাফল আপডেটের ব্যবস্থা করা যায়।

## পরবর্তী বাস্তবায়ন
1. Firebase Auth-এ সত্যিকারের sign-up/sign-in ও session state
2. Firestore থেকে টুর্নামেন্ট লোড ও প্লেয়ার রেজিস্ট্রেশন
3. Backend-ভিত্তিক admin role এবং admin dashboard
4. Payment provider + backend verification
5. FCM token registration ও push notifications
6. Match result updates, validation, error/loading states এবং tests

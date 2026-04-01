# 整点报时应用 (Hourly Chime)

一个具有后台运行和整点报时功能的Android移动应用。

## 功能特性

- **后台运行**: 应用在后台状态下仍能保持运行
- **整点报时**: 每个整点时刻发出提示音和通知
- **开机自启**: 设备重启后自动启动服务
- **振动提醒**: 整点时设备会振动提醒

## 技术要求

- Android 6.0 (API 23) 及以上
- Java 8
- Gradle 7.5

## 项目结构

```
HourlyChime/
├── .github/workflows/    # GitHub Actions 自动编译配置
├── app/
│   ├── src/main/
│   │   ├── java/com/hourlychime/app/
│   │   │   ├── MainActivity.java          # 主界面
│   │   │   ├── ChimeService.java          # 后台服务
│   │   │   ├── AlarmReceiver.java        # 闹钟接收器
│   │   │   └── BootReceiver.java         # 开机启动接收器
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   ├── values/
│   │   │   └── mipmap-anydpi-v26/
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## 自动编译

项目配置了GitHub Actions，当代码推送到GitHub时会自动触发编译流程，生成APK文件。

## 权限说明

应用需要以下权限：
- `FOREGROUND_SERVICE`: 前台服务权限
- `POST_NOTIFICATIONS`: 发送通知权限
- `VIBRATE`: 振动权限
- `WAKE_LOCK`: 唤醒锁权限
- `RECEIVE_BOOT_COMPLETED`: 开机启动权限

## 使用说明

1. 打开应用
2. 点击"启用整点报时"按钮
3. 授予通知权限
4. 应用将在后台运行，每小时整点提醒

## 构建说明

### 本地构建

```bash
# 构建 Debug 版本
./gradlew assembleDebug

# 构建 Release 版本
./gradlew assembleRelease
```

### 自动构建

推送到GitHub后，GitHub Actions会自动构建APK文件，可以在Actions页面下载。

## 许可证

MIT License

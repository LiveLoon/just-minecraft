

#  JustMC · 我的世界公益服务器

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.4-brightgreen?logo=minecraft)
![Status](https://img.shields.io/badge/Status-Online-2ea44f)
![License](https://img.shields.io/badge/License-MIT-blue)

> **"JustMC"** —— 一个纯净、公益、社区维护的我的世界服务器。

JustMC 是一个运行于 **Minecraft 26.2** 的服务器，无模组、无商业化。服务器 24/7 运行，需正版账户。

---

## 📖 目录

- [服务器特色](#-服务器特色)
- [快速开始](#-快速开始)
- [目录结构](#-目录结构)
- [部署与维护](#-部署与维护)
- [贡献与参与](#-贡献与参与)
- [免责声明](#-免责声明)
- [许可证](#-许可证)

---

## 🌟 服务器特色

- **无模组** – 服务器不添加模组。
- **公益免费** – 不收取任何费用，由JustMC 社区共同维护。
- **正版验证** – 仅限正版账户登录。
- **自动备份** – 每周自动备份世界数据，并且玩家也会下载世界存档进行保存，进一步保证了存档的安全性。
- **玩家共建** – 服主不干涉正常游戏行为，但保留对违规行为的处置权。

---

## 🚀 快速开始

### 玩家加入
1. 启动 Minecraft（Java Edition）并登录正版账户。
2. 添加服务器地址 （进QQ群了解 ：661436985 ）

### 管理员部署（自行搭建）
若您想基于此项目搭建自己的服务器，请继续阅读以下部署指南。

---

## 📁 目录结构

以下是本仓库的主要目录和文件说明：

```bash
just-minecraft/
├── backups/                  # 世界备份压缩包（自动生成）
├── config/                   # Paper 服务器核心配置
│   ├── paper-global.yml
│   └── paper-world-defaults.yml
├── logs/                     # 服务器日志（已压缩归档）
├── plugins/                  # 插件安装目录
│   ├── BlueMap/              # 地图插件配置
│   ├── Essentials/           # 基础管理插件
│   ├── LuckPerms/            # 权限管理
│   └── ...                   # 其他插件（详见插件列表）
├── world/                    # 主世界数据（含 region, entities, poi）
├── world_the_end/            # 末地世界
├── world_the_nether/         # 地狱世界
├── server.properties         # 服务器核心配置
├── paper-26.2-40.jar         # Paper 服务端核心
├── just-mc-server-1.0-SNAPSHOT.jar  # 自定义后端服务
├── docker-compose.yml        # Docker Compose 部署文件
├── Dockerfile                # 容器构建文件
├── start.sh                  # （示例）启动脚本
└── README.md                 # 本文件
```



---

## 🛠️ 部署与维护

### 环境要求
- **Java 21+**（推荐使用 OpenJDK 21）
- **Docker & Docker compose** 
- **系统**：Linux / Windows / macOS（推荐 Linux 生产环境）
- **内存**：至少 4GB（建议 8GB+）
- **存储**：至少 20GB（含世界数据）

### 使用 Docker Compose 快速启动
```bash
git clone https://github.com/your-username/justmc-server.git
cd justmc-server
docker-compose up -d
```

> 注意：首次启动前，请编辑 `server.properties` 和 `docker-compose.yml` 中的端口映射、内存分配等参数。

### 手动启动
```bash
java -Xms4G -Xmx8G -jar paper-26.2-40.jar nogui
```

---

## 🤝 贡献与参与

- **玩家交流**：加入 QQ 群 **661436985**
- **问题反馈**：请在 QQ 群或者 GitHub Issues 中提交，或联系服主。
- **贡献代码/插件**：欢迎提交 Pull Request，但请先阅读贡献指南。

---

## ⚠️ 免责声明

本项目为 **个人公益性质**，服主不对服务器可用性、数据完整性及玩家损失承担任何责任。详细条款请参阅 [免责协议](JustMC服务器条款v26.2.md)。

---

## 📜 许可证

本仓库的配置文件和脚本以 **MIT License** 开源，Minecraft 服务端及其插件遵循各自的许可证。

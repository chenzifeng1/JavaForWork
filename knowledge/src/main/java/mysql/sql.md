# SQL demo

## 建表

```sql
CREATE TABLE `user` (
  `id` int NOT NULL COMMENT '主键',
  `name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '姓名',
  `age` int DEFAULT NULL COMMENT '年龄',
  `address` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址',
  `email` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(11) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '电话',
  `id_card` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '身份证',
  `account` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '账号',
  `password` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
```

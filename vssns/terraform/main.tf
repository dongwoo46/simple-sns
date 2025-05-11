provider "kafka" {
  bootstrap_servers = ["kafka:9092"]
}

# 1:1 채팅용 토픽
resource "kafka_topic" "chat_dm" {
  name               = "chat.dm"
  partitions         = 10   # 유저 수 or 예상 병렬 수 만큼 설정
  replication_factor = 1
}

# 그룹 채팅용 토픽
resource "kafka_topic" "chat_group" {
  name               = "chat.group"
  partitions         = 5   # 채팅방 수 기준으로 설정 가능
  replication_factor = 1
}

# 알림용 토픽 (유저 알림, 시스템 알림 등 포함)
resource "kafka_topic" "notification" {
  name               = "notification"
  partitions         = 5
  replication_factor = 1
}
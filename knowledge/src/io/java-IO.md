# Java IO

### BuffedIO与普通IO 谁更快
BufferIO更快，8K的数据缓冲区.

- ByteBuffer:
三个指针： pos:读写指针，读写操作开始的指针 limit:读操作时，lim会指向有效数据的最后一位。写操作时，lim=cap cap:buffer分配的内存区容量

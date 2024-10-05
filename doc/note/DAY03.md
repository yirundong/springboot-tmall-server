# 8. 关于创建数据表

## 8.1. 商品表

```mysql
CREATE TABLE mall_goods (
    id bigint unsigned auto_increment not null COMMENT '数据ID',
    category_id bigint unsigned DEFAULT 0 COMMENT '类别ID',
    category_name varchar(32) DEFAULT '' COMMENT '类别名称',
    title varchar(255) DEFAULT '' COMMENT '标题',
    brief varchar(255) DEFAULT '' COMMENT '摘要',
    cover_url varchar(255) DEFAULT '' COMMENT '封面图',
	sale_price decimal(10, 2) DEFAULT 0 COMMENT '价格',
    keywords varchar(255) DEFAULT '' COMMENT '关键词列表',
	is_recommend tinyint unsigned DEFAULT 0 COMMENT '是否推荐',
    check_state tinyint unsigned DEFAULT 0 COMMENT '审核状态',
    is_put_on tinyint unsigned DEFAULT 0 COMMENT '是否上架',
    sort tinyint unsigned DEFAULT 0 COMMENT '排序序号',
    gmt_create datetime default null COMMENT '数据创建时间',
    gmt_modified datetime default null COMMENT '数据最后修改时间',
    PRIMARY KEY (id)
) COMMENT '商品' DEFAULT CHARSET = utf8mb4;
```

关于以上设计：

- 无论表中的数据量有没有可能超过`int`的上限值，ID都使用`bigint`类型
- 关于整型数据的`unsigned`，表示此字段的数据是“无符号位的”，以`tinyint`（对应Java中的`byte`）为例，在有符号位时，取值区间为`[-128, 127]`，如果设置为无符号位，取值区间为`[0, 255]`，其实，许多整型字段添加`unsigned`更多的只是为了表示语义：“此字段的值不可能为负数”，并一定是为了得到更大的正数的取值区间
- 关于`varchar`类型的设置值，应该设计一个比你当下认为的上限值更大的值，但不要过份夸张，避免产生歧义
- 所有表示状态的字段（例如：是否删除、是否启用、是否推荐，甚至包括审核状态、订单状态等，甚至包括用户的性别）都应该使用数值型的类型，通常使用`tinyint`就够了，切忌使用字符类型来存储状态

- `DEFAULT CHARSET=???`和`CHARSET=???`是等效的
- 在当下使用较多的MySQL / MariaDB的版本中，`utf8`指的是`utf8mb3`（most bytes 3），即最多使用3个字节来记录1个字符，可以表示绝大部分正常使用的字符，但是，某些特殊的生僻字、特殊符号、emoji表情等无法表示，而使用`utf8mb4`（most bytes 4）会最多使用4个字节来记录1个字节，可以表示更多字符



## 附：数据库中`char`与`varchar`的区别（面试题）

在数据库中，`char`和`varchar`都是用于存储较短的字符串类型的数据的（较长的字符串需要使用`text`）！

其中，`char`是定长（长度固定）的，例如，某个字段设计为`char(16)`，如果在此字段中存入`hello`（长度为5），则会自动补充11个空格再存入到此字段中，而`varchar`中变长（长度可变）的，例如，某个字段设计为`varchar(16)`，如果在此字段中存入`hello`，则只会将`hello`存入，占用5个字符对应的存储空间！

注意：无论是`char`还是`varchar`，其设置值都是“字符数量”，具体占用多少字节，需要结合此字段的值的字符编码来计算！

对于`varchar`类型的数据，默认情况下，数据库会自动的额外使用1个字节来记录“实际存入的字符数量”，例如在`varchar(16)`中存入了`hello`，则数据库会自动的额外使用1个字节记录下`5`这个值（`hello`的字符数量），后续，在读取此字段的值时，会先读取到这个`5`，然后从此字段读`5`个字符出来，由于1个字节能表示的数字的上限是255，如果`varchar`被设计得更大，允许存放更多内容，例如设计为`varchar(2000)`，并且，实际存入的字符数量超过了255个，则数据库会自动的改为使用2个字节来记录实际存入的字符数量！

由于`char`是定长的，所以，并不需要使用额外的字节来记录实际存入的字符数量，并且，在读取数据时，直接按照`char`的设计值直接读取数据即可！

所以，相比之下，`varchar`需要额外的消耗1~2个字节来记录实际存入的字符数量，并且，在读取时没有`char`那么便捷！
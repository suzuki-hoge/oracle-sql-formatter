# Select
とりあえずクエリサンプルを適当に持ってきて考えてみる

## SELECtとFROM
### クエリ
#### カラムをカンマ区切り
```Sql
SELECT name, job, FROM emp;
```

#### 全部のカラム
```Sql
SELECT * FROM emp;
```

#### ALL/DISTINCT
```Sql
SELECT ALL job FROM emp;

SELECT DISTINCT job FROM emp;
```

`ALL`か`DISTINCT`を用いる
省略した場合は`ALL`

### BNF
`SELECT カラム FROM テーブル名;`が最小構成

+ `テーブル名`は1-30文字の単語
 + 利用可能英数字のみの30文字以下は頻出するので抽象化する
+ `カラム`は以下の2パターン
 + `ALL/DISTINCT カラム名`
 + `*`

ここまでを起こすと以下

```
SelectQuery ::= 'SELECT' SelectColumns 'FROM' ValidName ';'

ValidName ::= [A-Za-z0-9_-]{1,30}
ValidNames ::= ValidName (',' ValidName)*

SelectColumns ::= '*' | ('ALL' | 'DISTINCT')? ValidNames
```

## WHERE
### SQL
`where`に続けて条件を書く

#### 比較演算子
`=`, `<`, `>`, `<=`, `>=`, `!=`, `^=`, `<>`

```Sql
SELECT * FROM emp WHERE deptno = 10;
SELECT * FROM emp WHERE sal >= 5000;
SELECT * FROM emp WHERE deptno != 10;
```

+ カラム名と値を比べる

#### IN
```Sql
SELECT * FROM emp WHERE empnu IN (7369,7499,7521);
SELECT * FROM emp WHERE job IN ('CLERK','ANALYST');
```

`NOT`を付けることが出来る -> `NOT IN`

#### BETWEEN
```Sql
SELECT * FROM emp WHERE sal BETWEEN 1000 AND 2000;
```

`NOT`を付けることが出来る -> `NOT BETWEEN`

#### IS
```Sql
SELECT * FROM emp WHERE mgr IS NULL;
```

`NOT`を付けることが出来る -> `IS NOT`

#### ALL/ANY/SOME
```Sql
SELECT * FROM dept WHERE loc = SOME ('NEW YORK', 'DALLAS');
```

#### LIKE
正規表現とか`ESCAPE`とか大変そうなので一旦後回し

#### AND/OR
```Sql
SELECT * FROM emp WHERE deptno = 10 AND sql >= 5000;
SELECT * FROM emp WHERE deptno = 10 OR sql >= 5000;
```

### BNF
`WHERE 条件;`が最小構成

条件は以下に分類される

+ 比較演算子
+ `IN`
+ `BETWEEN`
+ `IS`
+ `ALL`, `ANY`, `SOME`
+ ~~`LIKE`~~

条件は`AND`, `OR`で結合する事が出来る

値は計算結果でも良い気がするが、一旦考えないことにする

ここまでを起こすと以下

```
WhereCondition ::= 'WHERE' Condition (('AND' | 'OR') Condition)* 
Condition ::= ComparisonCondition | InCondition | BetweenCondition | IsCondition | PluralCondition

Value ::= [a-zA-Z]+ | [1-9][0-9]*
Values ::=  Value (',' Value)*

Operator ::= ('=' | '<' | '>' | '<=' | '>=' | '!=' | '^=' | '<>')
ComparisonCondition ::= ValidName Operator Value
InCondition ::= ValidName 'NOT'? ~ 'IN' '(' Values ')'
BetweenCondition ::= ValidName 'NOT'? 'BETWEEN' Value 'AND' Value
IsCondition ::= Value 'IS' 'NOT'? 'NULL'
PluralCondition ::= ValidName Operator ('ALL' | 'ANY' | 'SOME') '(' Values ')'
```

`SelectQuery`と結合

```
SelectQuery ::= 'SELECT' SelectColumns 'FROM' ValidName WhereCondition?';'
```

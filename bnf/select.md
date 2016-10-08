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

+ `SELECT`, `FROM`, `;`はそれぞれ文字列
 + **todo:** BNFって文字列の大文字小文字はどうなるの？
+ `テーブル名`は1-30文字の単語
 + **todo:** 利用可能な英数字って？
 + 利用可能英数字のみの30文字以下は頻出するので抽象化する
+ `カラム`は以下の2パターン
 + `ALL/DISTINCT カラム名`
 + `*`

ここまでを起こすと以下

```
SelectQuery ::= 'SELECT' SelectColumns 'FROM' ValidName ';'

ValidName ::= todo 英数字30
ValidNames ::= ValidName (',' ValidName)*

SelectColumns ::= '*' | ('ALL' | 'DISTINCT')? ValidNames
```

#### Todo
+ 任意長の空白ってどうなるの？
 + そういうものを定義するの？
+ `('ALL' | 'DISTINCT')?`とか`(',' ValidName)*`みたいな`()`ってあり？
+ カンマで繋ぐカラム数って上限ある？

## WHERE
### SQL
`where`に続けて条件を書く

#### 比較演算子
`=`, `<`, `>`, `<=`, `>=`, `!=`

```Sql
SELECT * FROM emp WHERE deptno = 10;
SELECT * FROM emp WHERE sal >= 5000;
SELECT * FROM emp WHERE deptno != 10;
```

+ カラム名と値を比べる
+ カラム名と式?を比べる
+ **todo** カラム名とカラム名で比較って出来るの？

#### IN
```Sql
SELECT * FROM emp WHERE empnu IN (7369,7499,7521);
SELECT * FROM emp WHERE job IN ('CLERK','ANALYST');
```

#### BETWEEN
```Sql
SELECT * FROM EMP WHERE SAL BETWEEN 1000 AND 2000;
```

#### IS

```Sql
SELECT * FROM EMP WHERE MGR IS NULL;
```

#### LIKE
正規表現とか`ESCAPE`とか大変そうなので一旦後回し

#### AND/OR
```Sql
SELECT * FROM emp WHERE deptno = 10 AND sql >= 5000;
SELECT * FROM emp WHERE deptno = 10 OR sql >= 5000;
```

#### NOT
条件には`NOT`を付けられる

```Sql
SELECT * FROM emp WHERE NOT (deptno = 10);
```

### BNF
`WHERE 条件;`が最小構成

条件は以下に分類される

+ 比較演算子
+ `IN`
+ `BETWEEN`
+ `IS`
+ ~~`LIKE`~~

条件には`NOT`を付ける事が出来、`AND`, `OR`で結合する事も出来る

ここまでを起こすと以下

```
WhereCondition ::= 'WHERE' Condition (('AND' | 'OR') Condition)* 
Condition ::= 'NOT'? (OperatorCondition | InCondition | BetweenCondition | IsCondition)

Value ::= todo 値とか式とかそんなん
Values :: =  Value (',' Value)*

OperatorCondition ::= ValidName ('=' | '<' | '>' | '<=' | '>=' | '!=') Value
InCondition ::= ValidName 'IN' '(' Values ')'
BetweenCondition ::= 'BETWEEN' Value 'AND' Value
IsCondition ::= Value 'IS' 'NULL'
```

`SelectQuery`と結合

```
SelectQuery ::= 'SELECT' SelectColumns 'FROM' ValidName WhereCondition?';'
```

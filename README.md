# Oracle SQL Formatter

タイトルの通り、OracleのSQLのフォーマッタを作ってみようというお題

### 関連ワード
+ [バッカス・ナウア記法](https://ja.wikipedia.org/wiki/%E3%83%90%E3%83%83%E3%82%AB%E3%82%B9%E3%83%BB%E3%83%8A%E3%82%A6%E3%82%A2%E8%A8%98%E6%B3%95)
+ パーサ・コンビネータ

どれも初耳、ついでにOracleもほぼ経験無し

ある程度構造立てられた文法がぐぐれば出る気がするけど、まっさらなところから起こしてみたい

やることは以下

1. BNFを書く
+ Scalaのコードに変換する
+ 出力整形をする

### docs
+ [select](bnf/select.md)

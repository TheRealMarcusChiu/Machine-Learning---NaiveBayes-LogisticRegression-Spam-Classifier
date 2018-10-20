How to run this project
- download java 8 or higher, i am using java 10
- download maven 

Building/Compiling
- mvn clean package

Running Naive Bayes
- with stop-words
  - mvn exec:java -Dexec.mainClass="Main" -Dexec.args="nbc false"
- without stop-words
  - mvn exec:java -Dexec.mainClass="Main" -Dexec.args="nbc true"

Running Logistic Regression
- Wj := Wj - (α/m) * [ (∑(h(Xⁱ) - yⁱ)Xjⁱ) + λWj ]
- mvn exec:java -Dexec.mainClass="Main" -Dexec.args="lrc boolean-removeStopWords int-iterations double-alpha double-lambda"

- mvn exec:java -Dexec.mainClass="Main" -Dexec.args="lrc true 0 0.01 0.0"
  - remove stop-words
  - iterations = 0
  - α = 0.01
  - λ = 0.0
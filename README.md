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
- all weights initialized to 0.001
- 1 iteration takes around ~2-3 minutes
- Wj := Wj - (α/m) * [ (∑(h(Xⁱ) - yⁱ)Xjⁱ) + λWj ]
- mvn exec:java -Dexec.mainClass="Main" -Dexec.args="lrc boolean-removeStopWords int-iterations double-alpha double-lambda"

- mvn exec:java -Dexec.mainClass="Main" -Dexec.args="lrc true 0 0.01 0.0"
  - remove stop-words
  - iterations = 0
  - α = 0.01
  - λ = 0.0
  
SEMI DETAILED WRITE UP
- Stop Words
  - naive bayes
    - stop-words:           0.9456066945606695
    - stop-words removed:   0.9393305439330544
  - logistic regression
    - iterations=0 alpha=0.01 lambda=0
      - stop-words:         0.2719665271966527
      - stop-words removed: 0.2719665271966527
    - iterations=1 alpha=0.01 lambda=0
      - stop-words:         0.7280334728033473
      - stop-words removed: 0.8179916317991632
    - iterations=5 alpha=0.01 lambda=0
      - stop-words:         0.799163179916318
      - stop-words removed: 0.8263598326359832

  I don't think removing stop words helped in classifying whether an email is spam or not. even though removing stop words for the logistic regression part helped, this is because I see most spam email are broken english. I would think real spam would have less of those broken english text. However, the more iterations ran the less the gap between accuracy.
  
- Lambda 
  - with stop-words
      - iterations=1 alpha=0.01 lambda=5-5000000000
        - 0.7280334728033473

  - without stop words
      - iterations=1 alpha=0.01 lambda=0-500
        - 0.8179916317991632
      - iterations=1 alpha=0.01 lambda=500000-5000000000
        - 0.7280334728033473

- Alpha
  - without stop words
      - iterations=1 alpha=0.01 lambda=0
        - 0.8179916317991632
      - iterations=1 alpha=1000 lambda=0
        - 0.7280334728033473
        
- Alpha Lambda
  - without stop-words
    - iteration=1 alpha=999999999999999999 lambda=9999999999999999999
      - 0.7280334728033473
      
      
These Lambda and Alpha values are confusing me. I can only conclude that running Logistic Regression with different values of alpha and lambda is independent of the accuracies of the given test examples. maybe there is not enough, or maybe since my implementation of algorithm contains 9000+ inputs for both X and its Weights. I would next test different or a much larger data-set
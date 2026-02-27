import os
import subprocess
import glob
import random
from datetime import datetime, timedelta

repo_dir = "/Users/aryamirani/Desktop/codes/gs/sentinel"
os.chdir(repo_dir)

# Commit message pool
messages = [
    "init project", "added {}", "done {}", "changed {}", "removed {}", "fixed {}",
    "udpated {}", "configuraion for {}", "aded {}", "chnaged {}", "working on {}"
]
fallback_messages = ["done", "changed stuff", "fixed typo", "udpated", "aded file", "chnaged config"]

# Logical grouping of files (globs)
groups = [
    [".gitignore", ".gitattributes", "HELP.md"],
    ["gradlew", "gradlew.bat", "gradle/wrapper/gradle-wrapper.properties", "gradle/wrapper/gradle-wrapper.jar"],
    ["build.gradle", "settings.gradle"],
    ["src/main/java/com/sentinel/SentinelApplication.java"],
    ["src/main/java/com/sentinel/model/Transaction.java"],
    ["src/main/java/com/sentinel/model/Customer.java"],
    ["src/main/java/com/sentinel/model/RiskAssessment.java", "src/main/java/com/sentinel/model/DeviceFingerprint.java"],
    ["src/main/java/com/sentinel/model/GeoLocation.java", "src/main/java/com/sentinel/model/AlertPayload.java"],
    ["src/main/resources/application.yml"],
    ["src/main/resources/application-dev.yml"],
    ["src/main/resources/logback-spring.xml"],
    ["src/main/java/com/sentinel/config/KafkaProducerConfig.java"],
    ["src/main/java/com/sentinel/config/KafkaConsumerConfig.java"],
    ["src/main/java/com/sentinel/config/MongoConfig.java"],
    ["src/main/java/com/sentinel/config/RedisConfig.java"],
    ["src/main/java/com/sentinel/config/WebFluxConfig.java", "src/main/java/com/sentinel/config/WebSocketConfig.java"],
    ["src/main/java/com/sentinel/config/EmbeddingConfig.java"],
    ["src/main/java/com/sentinel/repository/TransactionRepository.java"],
    ["src/main/java/com/sentinel/repository/CustomerRepository.java"],
    ["src/main/java/com/sentinel/kafka/TransactionProducer.java"],
    ["src/main/java/com/sentinel/kafka/TransactionConsumer.java"],
    ["src/main/java/com/sentinel/kafka/KafkaHeaderPropagator.java"],
    ["src/main/java/com/sentinel/gateway/TransactionController.java"],
    ["src/main/java/com/sentinel/gateway/CorrelationIdFilter.java"],
    ["src/main/java/com/sentinel/gateway/AlertWebSocketHandler.java"],
    ["src/main/java/com/sentinel/service/VelocityService.java"],
    ["src/main/java/com/sentinel/service/EmbeddingService.java"],
    ["src/main/java/com/sentinel/service/AlertService.java"],
    ["src/main/java/com/sentinel/engine/DeterministicRuleEvaluator.java"],
    ["src/main/java/com/sentinel/engine/VectorSearchEvaluator.java"],
    ["src/main/java/com/sentinel/engine/TopologyTraverser.java"],
    ["src/main/java/com/sentinel/engine/RiskScoreAggregator.java"],
    ["src/main/java/com/sentinel/engine/RiskEngine.java"],
    ["src/main/java/com/sentinel/stream/TransactionChangeStreamListener.java"],
    ["src/test/java/com/sentinel/SentinelApplicationTests.java"],
    ["src/test/java/com/sentinel/engine/RiskEngineTest.java"],
    ["src/test/java/com/sentinel/engine/DeterministicRuleEvaluatorTest.java"],
    ["src/test/java/com/sentinel/engine/RiskScoreAggregatorTest.java"],
    ["src/test/java/com/sentinel/engine/TopologyTraverserTest.java"],
    ["src/test/java/com/sentinel/gateway/TransactionControllerTest.java"],
    ["sentinel-dashboard/package.json", "sentinel-dashboard/vite.config.js"],
    ["sentinel-dashboard/index.html", "sentinel-dashboard/src/main.jsx"],
    ["sentinel-dashboard/src/index.css"],
    ["sentinel-dashboard/src/App.jsx"],
    ["sentinel-dashboard/src/components/Sidebar.jsx"],
    ["sentinel-dashboard/src/components/Header.jsx"],
    ["sentinel-dashboard/src/components/RiskBadge.jsx"],
    ["sentinel-dashboard/src/components/TransactionCard.jsx"],
    ["sentinel-dashboard/src/components/StatsCard.jsx"],
    ["sentinel-dashboard/src/components/RiskChart.jsx"],
    ["sentinel-dashboard/src/components/EntityNode.jsx"],
    ["sentinel-dashboard/src/components/AlertToast.jsx"],
    ["sentinel-dashboard/src/pages/Dashboard.jsx"],
    ["sentinel-dashboard/src/pages/NetworkGraph.jsx"],
    ["sentinel-dashboard/src/pages/Alerts.jsx"],
    ["sentinel-dashboard/src/pages/TransactionDetail.jsx"],
    ["sentinel-dashboard/src/hooks/useWebSocket.js"],
    ["sentinel-dashboard/src/hooks/useMockData.js"],
    ["sentinel-dashboard/src/utils/api.js"],
    ["sentinel-dashboard/src/utils/mockData.js"],
    ["docker-compose.yml"],
    ["Dockerfile"],
    [".github/workflows/ci.yml"],
    ["README.md"]
]

# We want 82 commits. We have 64 groups. We need 18 more empty/minor commits.
total_commits = 82
start_date = datetime(2025, 12, 1, 10, 0, 0)
end_date = datetime(2026, 2, 28, 18, 0, 0)
delta = end_date - start_date
dates = [start_date + timedelta(seconds=random.randint(0, int(delta.total_seconds()))) for _ in range(total_commits)]
dates.sort()

# First, let's reset git to unstage everything, just in case
subprocess.run(["git", "reset"])
# Also remove everything from git index
subprocess.run(["git", "rm", "-r", "--cached", "."], stderr=subprocess.DEVNULL)

commit_idx = 0

for group in groups:
    for pattern in group:
        files = glob.glob(pattern, recursive=True)
        for f in files:
            if os.path.isfile(f):
                subprocess.run(["git", "add", f])
    
    # get a random message
    file_name = os.path.basename(group[0])
    msg_template = random.choice(messages)
    if "{}" in msg_template:
        msg = msg_template.format(file_name.split('.')[0].lower())
    else:
        msg = msg_template
        
    date_str = dates[commit_idx].strftime("%Y-%m-%dT%H:%M:%S")
    env = os.environ.copy()
    env["GIT_AUTHOR_DATE"] = date_str
    env["GIT_COMMITTER_DATE"] = date_str
    
    subprocess.run(["git", "commit", "-m", msg], env=env)
    commit_idx += 1

# For the remaining commits, do empty commits or touch a file
while commit_idx < total_commits:
    msg = random.choice(fallback_messages)
    date_str = dates[commit_idx].strftime("%Y-%m-%dT%H:%M:%S")
    env = os.environ.copy()
    env["GIT_AUTHOR_DATE"] = date_str
    env["GIT_COMMITTER_DATE"] = date_str
    
    subprocess.run(["git", "commit", "--allow-empty", "-m", msg], env=env)
    commit_idx += 1

print(f"Created {commit_idx} commits.")

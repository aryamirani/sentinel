const merchants = [
  'Apex Trading Corp', 'Digital Assets Exchange', 'Offshore Holdings Ltd',
  'Global Remittance Hub', 'International Wire Svc', 'Crypto Bridge Network',
  'Pacific Commerce Ltd', 'Atlas Financial Group', 'Meridian Payments Inc',
  'Sterling Transfer Co', 'Vanguard Trading LLC', 'Eclipse Fund Services',
  'Quantum Capital Partners', 'Nexus Payment Solutions', 'Orion Wealth Management'
]

const categories = [
  'Wire Transfer', 'ACH Payment', 'Card Payment', 'Crypto Exchange',
  'International Remittance', 'P2P Transfer', 'Bill Payment'
]

const actions = ['ALLOW', 'ALLOW', 'ALLOW', 'ALLOW', 'REVIEW', 'REVIEW', 'BLOCK']
const ips = ['185.220.101.42', '103.28.76.11', '91.108.56.200', '45.33.120.89', '192.168.1.100', '10.0.0.55', '172.16.0.12']

function randomItem(arr) {
  return arr[Math.floor(Math.random() * arr.length)]
}

function randomBetween(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min
}

export function generateTransactions(count = 20) {
  const transactions = []
  const now = Date.now()

  for (let i = 0; i < count; i++) {
    const action = randomItem(actions)
    const ruleScore = action === 'BLOCK' ? randomBetween(50, 95) : randomBetween(0, 40)
    const vectorScore = action === 'BLOCK' ? randomBetween(40, 90) : randomBetween(0, 30)
    const topologyScore = action === 'BLOCK' ? randomBetween(60, 100) : randomBetween(0, 25)
    const totalScore = (ruleScore * 0.35 + vectorScore * 0.25 + topologyScore * 0.40)

    const flags = []
    if (ruleScore > 30) flags.push('VELOCITY_ANOMALY')
    if (vectorScore > 30) flags.push('BEHAVIORAL_DEVIATION')
    if (topologyScore > 30) flags.push('TOPOLOGY_RISK')

    transactions.push({
      id: `txn-${8850 - i}`,
      entityId: `entity-${randomBetween(1000, 9999)}`,
      amount: randomBetween(50, 25000),
      currency: 'USD',
      merchantName: randomItem(merchants),
      merchantCategory: randomItem(categories),
      ipAddress: randomItem(ips),
      timestamp: new Date(now - i * randomBetween(120000, 600000)).toISOString(),
      status: action,
      riskAssessment: {
        totalScore: Math.round(totalScore * 10) / 10,
        ruleScore,
        vectorScore,
        topologyScore,
        action,
        flags,
        evaluationTimeMs: randomBetween(12, 85),
      },
      device: {
        fingerprintHash: `fp-${Math.random().toString(36).substring(2, 10)}`,
        os: randomItem(['iOS 17', 'Android 14', 'Windows 11', 'macOS 14']),
        browser: randomItem(['Chrome 120', 'Safari 17', 'Firefox 121', 'Edge 120']),
      },
    })
  }

  return transactions
}

export function generateTimeSeriesData(hours = 24) {
  const data = []
  const now = new Date()

  for (let i = hours; i >= 0; i--) {
    const time = new Date(now.getTime() - i * 3600000)
    data.push({
      time: time.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' }),
      riskScore: randomBetween(15, 65),
      volume: randomBetween(200, 900),
      blocked: randomBetween(0, 12),
    })
  }

  return data
}

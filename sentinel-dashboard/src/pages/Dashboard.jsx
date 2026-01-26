import { Activity, ShieldAlert, ShieldCheck, BarChart3 } from 'lucide-react'
import StatsCard from '../components/StatsCard'
import RiskChart from '../components/RiskChart'
import TransactionCard from '../components/TransactionCard'
import { useMockData } from '../hooks/useMockData'

function Dashboard({ onSelectTransaction }) {
  const { transactions, timeSeriesData, stats } = useMockData()

  return (
    <div className="fade-in">
      <h1 className="page-title">Dashboard</h1>

      <div className="grid-4" style={{ marginBottom: '24px' }}>
        <StatsCard
          title="Total Transactions"
          value={stats.total}
          change={12.5}
          icon={Activity}
          color="99, 102, 241"
          gradient="var(--gradient-blue)"
        />
        <StatsCard
          title="Flagged"
          value={stats.flagged}
          change={-8.3}
          icon={ShieldAlert}
          color="245, 158, 11"
          gradient="var(--gradient-warning)"
        />
        <StatsCard
          title="Blocked"
          value={stats.blocked}
          change={23.1}
          icon={ShieldAlert}
          color="244, 63, 94"
          gradient="var(--gradient-danger)"
        />
        <StatsCard
          title="Allowed"
          value={stats.allowed}
          change={5.7}
          icon={ShieldCheck}
          color="16, 185, 129"
          gradient="var(--gradient-success)"
        />
      </div>

      <div className="grid-2" style={{ marginBottom: '24px' }}>
        <RiskChart data={timeSeriesData} title="Risk Score & Volume Timeline" />

        <div className="card">
          <div className="card-header">
            <span className="card-title">Risk Distribution</span>
            <BarChart3 size={16} color="var(--text-muted)" />
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', padding: '8px 0' }}>
            {[
              { label: 'Low Risk (0-30)', pct: 62, color: 'var(--accent-emerald)' },
              { label: 'Medium Risk (30-70)', pct: 24, color: 'var(--accent-amber)' },
              { label: 'High Risk (70-100)', pct: 14, color: 'var(--accent-rose)' },
            ].map(item => (
              <div key={item.label}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '6px' }}>
                  <span style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>{item.label}</span>
                  <span style={{ fontSize: '0.8rem', fontWeight: 600, color: item.color }}>{item.pct}%</span>
                </div>
                <div className="score-bar" style={{ height: '8px' }}>
                  <div className="score-fill" style={{
                    width: `${item.pct}%`,
                    background: item.color,
                  }} />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="card">
        <div className="card-header">
          <span className="card-title">Recent Transactions</span>
          <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
            Showing {transactions.length} latest
          </span>
        </div>
        <div className="transaction-list">
          {transactions.map(txn => (
            <TransactionCard
              key={txn.id}
              transaction={txn}
              onClick={onSelectTransaction}
            />
          ))}
        </div>
      </div>
    </div>
  )
}

export default Dashboard

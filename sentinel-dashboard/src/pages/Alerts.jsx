import { useState } from 'react'
import { AlertTriangle, Clock, DollarSign } from 'lucide-react'
import RiskBadge from '../components/RiskBadge'

const mockAlerts = [
  { alertId: 'alt-001', transactionId: 'txn-8842', entityId: 'entity-7291', riskScore: 92.4, action: 'BLOCK', reason: 'circular payment loop detected', amount: 12500, merchantName: 'Offshore Holdings Ltd', ipAddress: '185.220.101.42', timestamp: '2026-01-15T14:23:00Z' },
  { alertId: 'alt-002', transactionId: 'txn-8839', entityId: 'entity-3842', riskScore: 87.1, action: 'BLOCK', reason: 'velocity spike, unknown device', amount: 8750, merchantName: 'Digital Assets Exchange', ipAddress: '103.28.76.11', timestamp: '2026-01-15T14:18:00Z' },
  { alertId: 'alt-003', transactionId: 'txn-8835', entityId: 'entity-1055', riskScore: 65.3, action: 'REVIEW', reason: 'behavioral deviation from baseline', amount: 3200, merchantName: 'International Wire Svc', ipAddress: '91.108.56.200', timestamp: '2026-01-15T14:12:00Z' },
  { alertId: 'alt-004', transactionId: 'txn-8830', entityId: 'entity-6610', riskScore: 78.9, action: 'BLOCK', reason: 'multi-hop payment chain, shared device fingerprint', amount: 15000, merchantName: 'Apex Trading Corp', ipAddress: '185.220.101.42', timestamp: '2026-01-15T14:05:00Z' },
  { alertId: 'alt-005', transactionId: 'txn-8828', entityId: 'entity-4419', riskScore: 54.2, action: 'REVIEW', reason: 'amount spike above 2x historical mean', amount: 6800, merchantName: 'Global Remittance Hub', ipAddress: '45.33.120.89', timestamp: '2026-01-15T13:58:00Z' },
  { alertId: 'alt-006', transactionId: 'txn-8822', entityId: 'entity-2901', riskScore: 71.5, action: 'BLOCK', reason: 'tor exit node ip, emulator detected', amount: 4200, merchantName: 'Crypto Bridge Network', ipAddress: '185.220.102.8', timestamp: '2026-01-15T13:45:00Z' },
]

function Alerts({ alerts: liveAlerts }) {
  const [filter, setFilter] = useState('all')
  const allAlerts = [...(liveAlerts || []), ...mockAlerts]

  const filtered = filter === 'all'
    ? allAlerts
    : allAlerts.filter(a => a.action === filter)

  const formatTime = (ts) => {
    const d = new Date(ts)
    return d.toLocaleString('en-US', { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' })
  }

  return (
    <div className="fade-in">
      <h1 className="page-title">Risk Alerts</h1>

      <div className="filter-bar">
        {['all', 'BLOCK', 'REVIEW'].map(f => (
          <button
            key={f}
            className={`filter-btn ${filter === f ? 'active' : ''}`}
            onClick={() => setFilter(f)}
          >
            {f === 'all' ? 'All Alerts' : f}
            {f !== 'all' && ` (${allAlerts.filter(a => a.action === f).length})`}
          </button>
        ))}
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        {filtered.map((alert, idx) => (
          <div key={alert.alertId || idx} className="card slide-in" style={{
            animationDelay: `${idx * 50}ms`,
            borderLeft: `3px solid ${alert.action === 'BLOCK' ? 'var(--accent-rose)' : 'var(--accent-amber)'}`,
          }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <div style={{ display: 'flex', gap: '12px', alignItems: 'flex-start' }}>
                <AlertTriangle
                  size={20}
                  color={alert.action === 'BLOCK' ? 'var(--accent-rose)' : 'var(--accent-amber)'}
                  style={{ marginTop: '2px' }}
                />
                <div>
                  <div style={{ fontWeight: 600, marginBottom: '4px' }}>
                    {alert.reason}
                  </div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', display: 'flex', gap: '16px', flexWrap: 'wrap' }}>
                    <span>Entity: {alert.entityId}</span>
                    <span>TXN: {alert.transactionId}</span>
                    <span>IP: {alert.ipAddress}</span>
                  </div>
                  <div style={{ display: 'flex', gap: '16px', marginTop: '8px', fontSize: '0.8rem' }}>
                    <span style={{ display: 'flex', alignItems: 'center', gap: '4px', color: 'var(--text-muted)' }}>
                      <DollarSign size={14} />${alert.amount?.toLocaleString()}
                    </span>
                    <span style={{ display: 'flex', alignItems: 'center', gap: '4px', color: 'var(--text-muted)' }}>
                      <Clock size={14} />{formatTime(alert.timestamp)}
                    </span>
                  </div>
                </div>
              </div>
              <RiskBadge action={alert.action} score={alert.riskScore} />
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

export default Alerts

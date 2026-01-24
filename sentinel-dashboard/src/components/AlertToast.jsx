import { AlertTriangle, X } from 'lucide-react'

function AlertToast({ alerts }) {
  if (!alerts || alerts.length === 0) return null

  return (
    <div className="alert-toast">
      {alerts.map((alert, idx) => (
        <div key={alert.alertId || idx} className="toast-item">
          <div style={{ display: 'flex', alignItems: 'flex-start', gap: '12px' }}>
            <AlertTriangle size={18} color="var(--accent-rose)" style={{ marginTop: '2px', flexShrink: 0 }} />
            <div style={{ flex: 1 }}>
              <div style={{ fontWeight: 600, fontSize: '0.85rem', marginBottom: '4px' }}>
                Risk Alert — {alert.action}
              </div>
              <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', lineHeight: 1.4 }}>
                Score: {alert.riskScore?.toFixed(1)} · {alert.reason || 'anomalous pattern detected'}
              </div>
              <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', marginTop: '4px' }}>
                {alert.merchantName} · ${alert.amount?.toLocaleString()}
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
  )
}

export default AlertToast

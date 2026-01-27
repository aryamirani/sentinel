import { ArrowLeft, Shield } from 'lucide-react'
import RiskBadge from '../components/RiskBadge'

function TransactionDetail({ transaction, onBack }) {
  const assessment = transaction.riskAssessment || {}

  const scoreColor = (score) => {
    if (score > 70) return 'var(--accent-rose)'
    if (score > 30) return 'var(--accent-amber)'
    return 'var(--accent-emerald)'
  }

  return (
    <div className="fade-in">
      <button
        onClick={onBack}
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: '8px',
          background: 'none',
          border: 'none',
          color: 'var(--text-secondary)',
          cursor: 'pointer',
          fontSize: '0.85rem',
          fontFamily: 'Inter, sans-serif',
          marginBottom: '16px',
          padding: '4px 0',
        }}
      >
        <ArrowLeft size={16} /> Back to Dashboard
      </button>

      <div style={{ display: 'flex', alignItems: 'center', gap: '16px', marginBottom: '24px' }}>
        <h1 className="page-title" style={{ marginBottom: 0 }}>Transaction Detail</h1>
        <RiskBadge action={assessment.action || 'ALLOW'} score={assessment.totalScore} />
      </div>

      <div className="grid-2" style={{ marginBottom: '24px' }}>
        <div className="card">
          <div className="card-header">
            <span className="card-title">Transaction Info</span>
          </div>
          <div className="detail-grid">
            <div className="detail-field">
              <div className="detail-label">Transaction ID</div>
              <div className="detail-value">{transaction.id}</div>
            </div>
            <div className="detail-field">
              <div className="detail-label">Entity ID</div>
              <div className="detail-value">{transaction.entityId}</div>
            </div>
            <div className="detail-field">
              <div className="detail-label">Amount</div>
              <div className="detail-value">${transaction.amount?.toLocaleString()}</div>
            </div>
            <div className="detail-field">
              <div className="detail-label">Merchant</div>
              <div className="detail-value">{transaction.merchantName || 'N/A'}</div>
            </div>
            <div className="detail-field">
              <div className="detail-label">Category</div>
              <div className="detail-value">{transaction.merchantCategory || 'N/A'}</div>
            </div>
            <div className="detail-field">
              <div className="detail-label">IP Address</div>
              <div className="detail-value">{transaction.ipAddress || 'N/A'}</div>
            </div>
            <div className="detail-field">
              <div className="detail-label">Currency</div>
              <div className="detail-value">{transaction.currency || 'USD'}</div>
            </div>
            <div className="detail-field">
              <div className="detail-label">Status</div>
              <div className="detail-value">{transaction.status || 'PENDING'}</div>
            </div>
          </div>
        </div>

        <div className="card">
          <div className="card-header">
            <span className="card-title">Risk Assessment</span>
            <Shield size={16} color="var(--text-muted)" />
          </div>
          <div style={{ marginBottom: '20px' }}>
            <div style={{ fontSize: '2.5rem', fontWeight: 800, color: scoreColor(assessment.totalScore || 0) }}>
              {(assessment.totalScore || 0).toFixed(1)}
            </div>
            <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
              Overall Risk Score
            </div>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
            {[
              { label: 'Rule-Based Score', value: assessment.ruleScore || 0, weight: '35%' },
              { label: 'Vector Similarity', value: assessment.vectorScore || 0, weight: '25%' },
              { label: 'Topology Risk', value: assessment.topologyScore || 0, weight: '40%' },
            ].map(item => (
              <div key={item.label}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
                  <span style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>{item.label}</span>
                  <span style={{ fontSize: '0.8rem', fontWeight: 600, color: scoreColor(item.value) }}>
                    {item.value.toFixed(1)} <span style={{ color: 'var(--text-muted)', fontWeight: 400 }}>({item.weight})</span>
                  </span>
                </div>
                <div className="score-bar" style={{ height: '6px' }}>
                  <div className="score-fill" style={{
                    width: `${item.value}%`,
                    background: scoreColor(item.value),
                  }} />
                </div>
              </div>
            ))}
          </div>

          {assessment.flags && assessment.flags.length > 0 && (
            <div style={{ marginTop: '16px' }}>
              <div className="detail-label" style={{ marginBottom: '8px' }}>Triggered Flags</div>
              <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap' }}>
                {assessment.flags.map(flag => (
                  <span key={flag} style={{
                    padding: '4px 10px',
                    borderRadius: '4px',
                    background: 'rgba(244, 63, 94, 0.1)',
                    border: '1px solid rgba(244, 63, 94, 0.2)',
                    fontSize: '0.7rem',
                    fontWeight: 600,
                    color: 'var(--accent-rose)',
                  }}>
                    {flag}
                  </span>
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default TransactionDetail

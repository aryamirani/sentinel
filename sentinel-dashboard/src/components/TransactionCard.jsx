import RiskBadge from './RiskBadge'

function TransactionCard({ transaction, onClick }) {
  const formatAmount = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: transaction.currency || 'USD',
    }).format(amount)
  }

  const formatTime = (timestamp) => {
    const date = new Date(timestamp)
    return date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' })
  }

  return (
    <div className="transaction-item fade-in" onClick={() => onClick?.(transaction)}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
        <div style={{
          width: '40px',
          height: '40px',
          borderRadius: '10px',
          background: transaction.riskAssessment?.action === 'BLOCK'
            ? 'rgba(244, 63, 94, 0.1)'
            : 'rgba(99, 102, 241, 0.1)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          fontSize: '0.85rem',
          fontWeight: 700,
          color: transaction.riskAssessment?.action === 'BLOCK'
            ? 'var(--accent-rose)'
            : 'var(--accent-blue)',
        }}>
          {transaction.merchantName?.[0]?.toUpperCase() || 'T'}
        </div>
        <div>
          <div style={{ fontWeight: 500, fontSize: '0.9rem' }}>
            {transaction.merchantName || 'Unknown Merchant'}
          </div>
          <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
            {transaction.entityId} · {formatTime(transaction.timestamp)}
          </div>
        </div>
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
        <span style={{ fontWeight: 600, fontSize: '0.95rem' }}>
          {formatAmount(transaction.amount)}
        </span>
        <RiskBadge
          action={transaction.riskAssessment?.action || 'ALLOW'}
          score={transaction.riskAssessment?.totalScore}
        />
      </div>
    </div>
  )
}

export default TransactionCard

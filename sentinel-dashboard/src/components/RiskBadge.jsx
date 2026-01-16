function RiskBadge({ action, score }) {
  const config = {
    ALLOW: { className: 'badge-allow', label: 'Allow' },
    REVIEW: { className: 'badge-review', label: 'Review' },
    BLOCK: { className: 'badge-block', label: 'Block' },
  }

  const { className, label } = config[action] || config.ALLOW

  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
      <span className={`badge ${className}`}>
        {label}
      </span>
      {score !== undefined && (
        <span style={{
          fontSize: '0.75rem',
          color: 'var(--text-muted)',
          fontWeight: 600,
        }}>
          {score.toFixed(1)}
        </span>
      )}
    </div>
  )
}

export default RiskBadge

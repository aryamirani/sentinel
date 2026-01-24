import { Handle, Position } from '@xyflow/react'

const typeConfig = {
  customer: { color: '#6366f1', label: 'Customer' },
  transaction: { color: '#06b6d4', label: 'Transaction' },
  device: { color: '#a855f7', label: 'Device' },
  ip: { color: '#f59e0b', label: 'IP Address' },
  flagged: { color: '#f43f5e', label: 'Flagged' },
}

function EntityNode({ data }) {
  const config = typeConfig[data.type] || typeConfig.customer

  return (
    <div style={{
      background: 'var(--bg-card)',
      border: `2px solid ${data.flagged ? '#f43f5e' : config.color}`,
      borderRadius: 'var(--radius-md)',
      padding: '12px 16px',
      minWidth: '140px',
      backdropFilter: 'blur(12px)',
      boxShadow: data.flagged
        ? '0 0 20px rgba(244, 63, 94, 0.3)'
        : `0 0 15px ${config.color}22`,
      transition: 'all 0.2s ease',
    }}>
      <Handle type="target" position={Position.Top} style={{
        background: config.color,
        width: '8px',
        height: '8px',
        border: 'none',
      }} />

      <div style={{
        fontSize: '0.6rem',
        color: config.color,
        textTransform: 'uppercase',
        letterSpacing: '0.1em',
        fontWeight: 600,
        marginBottom: '6px',
      }}>
        {config.label}
      </div>

      <div style={{
        fontSize: '0.85rem',
        fontWeight: 600,
        color: 'var(--text-primary)',
        marginBottom: '4px',
      }}>
        {data.label}
      </div>

      {data.subLabel && (
        <div style={{
          fontSize: '0.7rem',
          color: 'var(--text-muted)',
        }}>
          {data.subLabel}
        </div>
      )}

      {data.score !== undefined && (
        <div style={{ marginTop: '8px' }}>
          <div className="score-bar">
            <div className="score-fill" style={{
              width: `${data.score}%`,
              background: data.score > 70 ? 'var(--gradient-danger)' :
                data.score > 30 ? 'var(--gradient-warning)' : 'var(--gradient-success)',
            }} />
          </div>
        </div>
      )}

      <Handle type="source" position={Position.Bottom} style={{
        background: config.color,
        width: '8px',
        height: '8px',
        border: 'none',
      }} />
    </div>
  )
}

export default EntityNode

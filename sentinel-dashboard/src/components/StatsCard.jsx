import { TrendingUp, TrendingDown } from 'lucide-react'

function StatsCard({ title, value, change, changeLabel, icon: Icon, color, gradient }) {
  const isPositive = change >= 0

  return (
    <div className="card fade-in" style={{ position: 'relative', overflow: 'hidden' }}>
      <div style={{
        position: 'absolute',
        top: '-20px',
        right: '-20px',
        width: '80px',
        height: '80px',
        borderRadius: '50%',
        background: gradient || 'var(--gradient-blue)',
        opacity: 0.08,
      }} />
      <div className="card-header">
        <span className="card-title">{title}</span>
        {Icon && (
          <div style={{
            width: '32px',
            height: '32px',
            borderRadius: '8px',
            background: `rgba(${color || '99, 102, 241'}, 0.12)`,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}>
            <Icon size={16} style={{ color: `rgb(${color || '99, 102, 241'})` }} />
          </div>
        )}
      </div>
      <div className="stat-value" style={{
        background: gradient || 'var(--gradient-blue)',
        WebkitBackgroundClip: 'text',
        WebkitTextFillColor: 'transparent',
        backgroundClip: 'text',
      }}>
        {typeof value === 'number' ? value.toLocaleString() : value}
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: '4px', marginTop: '8px' }}>
        {isPositive ? (
          <TrendingUp size={14} color="var(--accent-emerald)" />
        ) : (
          <TrendingDown size={14} color="var(--accent-rose)" />
        )}
        <span style={{
          fontSize: '0.75rem',
          fontWeight: 600,
          color: isPositive ? 'var(--accent-emerald)' : 'var(--accent-rose)',
        }}>
          {isPositive ? '+' : ''}{change}%
        </span>
        <span className="stat-label">{changeLabel || 'vs last hour'}</span>
      </div>
    </div>
  )
}

export default StatsCard

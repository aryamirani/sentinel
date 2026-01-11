import { Shield, LayoutDashboard, Network, Bell, Activity } from 'lucide-react'

const navItems = [
  { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { id: 'network', label: 'Network Graph', icon: Network },
  { id: 'alerts', label: 'Alerts', icon: Bell },
]

function Sidebar({ activePage, onNavigate }) {
  return (
    <aside style={{
      width: '240px',
      background: 'var(--bg-secondary)',
      borderRight: '1px solid var(--border-color)',
      display: 'flex',
      flexDirection: 'column',
      padding: '0',
      height: '100vh',
    }}>
      <div style={{
        padding: '24px 20px',
        display: 'flex',
        alignItems: 'center',
        gap: '12px',
        borderBottom: '1px solid var(--border-color)',
      }}>
        <div style={{
          width: '36px',
          height: '36px',
          borderRadius: '10px',
          background: 'var(--gradient-blue)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}>
          <Shield size={20} color="white" />
        </div>
        <div>
          <div style={{ fontWeight: 700, fontSize: '1.1rem' }}>Sentinel</div>
          <div style={{ fontSize: '0.65rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.1em' }}>
            Fraud Engine
          </div>
        </div>
      </div>

      <nav style={{ padding: '16px 12px', flex: 1 }}>
        {navItems.map(item => {
          const Icon = item.icon
          const isActive = activePage === item.id
          return (
            <button
              key={item.id}
              onClick={() => onNavigate(item.id)}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '12px',
                width: '100%',
                padding: '10px 12px',
                marginBottom: '4px',
                border: 'none',
                borderRadius: 'var(--radius-sm)',
                background: isActive ? 'rgba(99, 102, 241, 0.12)' : 'transparent',
                color: isActive ? 'var(--accent-blue)' : 'var(--text-secondary)',
                cursor: 'pointer',
                fontSize: '0.875rem',
                fontWeight: isActive ? 600 : 400,
                fontFamily: 'Inter, sans-serif',
                transition: 'all var(--transition-fast)',
                textAlign: 'left',
              }}
            >
              <Icon size={18} />
              {item.label}
            </button>
          )
        })}
      </nav>

      <div style={{
        padding: '16px 20px',
        borderTop: '1px solid var(--border-color)',
        display: 'flex',
        alignItems: 'center',
        gap: '8px',
      }}>
        <Activity size={14} color="var(--accent-emerald)" />
        <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>System Active</span>
      </div>
    </aside>
  )
}

export default Sidebar

import { useState } from 'react'
import Sidebar from './components/Sidebar'
import Header from './components/Header'
import Dashboard from './pages/Dashboard'
import NetworkGraph from './pages/NetworkGraph'
import Alerts from './pages/Alerts'
import TransactionDetail from './pages/TransactionDetail'
import AlertToast from './components/AlertToast'
import { useWebSocket } from './hooks/useWebSocket'

function App() {
  const [activePage, setActivePage] = useState('dashboard')
  const [selectedTransaction, setSelectedTransaction] = useState(null)
  const { alerts, connected } = useWebSocket()

  const renderPage = () => {
    if (selectedTransaction) {
      return (
        <TransactionDetail
          transaction={selectedTransaction}
          onBack={() => setSelectedTransaction(null)}
        />
      )
    }
    switch (activePage) {
      case 'dashboard':
        return <Dashboard onSelectTransaction={setSelectedTransaction} />
      case 'network':
        return <NetworkGraph />
      case 'alerts':
        return <Alerts alerts={alerts} />
      default:
        return <Dashboard onSelectTransaction={setSelectedTransaction} />
    }
  }

  return (
    <div className="app-layout">
      <Sidebar activePage={activePage} onNavigate={setActivePage} />
      <div className="main-content">
        <Header connected={connected} alertCount={alerts.length} />
        <div className="page-content">
          {renderPage()}
        </div>
      </div>
      <AlertToast alerts={alerts.slice(0, 3)} />
    </div>
  )
}

export default App

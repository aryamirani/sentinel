import { useState, useEffect, useRef, useCallback } from 'react'

export function useWebSocket(url = 'ws://localhost:8080/ws/alerts') {
  const [alerts, setAlerts] = useState([])
  const [connected, setConnected] = useState(false)
  const wsRef = useRef(null)
  const reconnectTimer = useRef(null)
  const reconnectDelay = useRef(1000)

  const connect = useCallback(() => {
    try {
      const ws = new WebSocket(url)
      wsRef.current = ws

      ws.onopen = () => {
        setConnected(true)
        reconnectDelay.current = 1000
      }

      ws.onmessage = (event) => {
        try {
          const alert = JSON.parse(event.data)
          setAlerts(prev => [alert, ...prev].slice(0, 100))
        } catch (e) {
          console.error('failed to parse alert', e)
        }
      }

      ws.onclose = () => {
        setConnected(false)
        scheduleReconnect()
      }

      ws.onerror = () => {
        ws.close()
      }
    } catch (e) {
      setConnected(false)
      scheduleReconnect()
    }
  }, [url])

  const scheduleReconnect = useCallback(() => {
    if (reconnectTimer.current) return
    reconnectTimer.current = setTimeout(() => {
      reconnectTimer.current = null
      reconnectDelay.current = Math.min(reconnectDelay.current * 2, 30000)
      connect()
    }, reconnectDelay.current)
  }, [connect])

  useEffect(() => {
    connect()
    return () => {
      if (wsRef.current) wsRef.current.close()
      if (reconnectTimer.current) clearTimeout(reconnectTimer.current)
    }
  }, [connect])

  return { alerts, connected }
}

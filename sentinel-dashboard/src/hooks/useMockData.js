import { useMemo } from 'react'
import { generateTransactions, generateTimeSeriesData } from '../utils/mockData'

export function useMockData() {
  const transactions = useMemo(() => generateTransactions(25), [])

  const timeSeriesData = useMemo(() => generateTimeSeriesData(24), [])

  const stats = useMemo(() => {
    const total = transactions.length
    const blocked = transactions.filter(t => t.riskAssessment?.action === 'BLOCK').length
    const flagged = transactions.filter(t => t.riskAssessment?.action === 'REVIEW').length
    const allowed = total - blocked - flagged
    return { total: 14832, flagged: 342, blocked: 89, allowed: 14401 }
  }, [transactions])

  return { transactions, timeSeriesData, stats }
}

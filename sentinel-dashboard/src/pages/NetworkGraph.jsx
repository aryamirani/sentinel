import { useCallback, useMemo } from 'react'
import {
  ReactFlow,
  Background,
  Controls,
  MiniMap,
  useNodesState,
  useEdgesState,
} from '@xyflow/react'
import '@xyflow/react/dist/style.css'
import EntityNode from '../components/EntityNode'

const nodeTypes = { entity: EntityNode }

const initialNodes = [
  { id: 'c1', type: 'entity', position: { x: 400, y: 50 }, data: { type: 'customer', label: 'Entity A-7291', subLabel: 'High Activity', score: 45 } },
  { id: 'c2', type: 'entity', position: { x: 100, y: 200 }, data: { type: 'customer', label: 'Entity B-3842', subLabel: 'Shell Account', score: 82, flagged: true } },
  { id: 'c3', type: 'entity', position: { x: 700, y: 200 }, data: { type: 'customer', label: 'Entity C-1055', subLabel: 'New Account', score: 35 } },
  { id: 'c4', type: 'entity', position: { x: 400, y: 350 }, data: { type: 'customer', label: 'Entity D-6610', subLabel: 'Flagged Network', score: 91, flagged: true } },
  { id: 't1', type: 'entity', position: { x: 250, y: 120 }, data: { type: 'transaction', label: '$12,500', subLabel: 'Wire Transfer' } },
  { id: 't2', type: 'entity', position: { x: 550, y: 120 }, data: { type: 'transaction', label: '$12,480', subLabel: 'ACH Payment' } },
  { id: 't3', type: 'entity', position: { x: 250, y: 280 }, data: { type: 'transaction', label: '$12,350', subLabel: 'Wire Transfer' } },
  { id: 't4', type: 'entity', position: { x: 550, y: 280 }, data: { type: 'transaction', label: '$11,900', subLabel: 'ACH Payment' } },
  { id: 'd1', type: 'entity', position: { x: 50, y: 400 }, data: { type: 'device', label: 'iPhone 15 Pro', subLabel: 'fp: a8f2...3e1d' } },
  { id: 'ip1', type: 'entity', position: { x: 700, y: 400 }, data: { type: 'ip', label: '185.220.101.x', subLabel: 'Tor Exit Node', flagged: true } },
  { id: 'c5', type: 'entity', position: { x: 400, y: 500 }, data: { type: 'customer', label: 'Entity E-4419', subLabel: 'Dormant', score: 15 } },
]

const initialEdges = [
  { id: 'e1', source: 'c1', target: 't1', animated: true, style: { stroke: '#6366f1', strokeWidth: 2 } },
  { id: 'e2', source: 't1', target: 'c2', animated: true, style: { stroke: '#f43f5e', strokeWidth: 2 } },
  { id: 'e3', source: 'c1', target: 't2', animated: true, style: { stroke: '#6366f1', strokeWidth: 2 } },
  { id: 'e4', source: 't2', target: 'c3', animated: true, style: { stroke: '#06b6d4', strokeWidth: 2 } },
  { id: 'e5', source: 'c2', target: 't3', animated: true, style: { stroke: '#f43f5e', strokeWidth: 2 } },
  { id: 'e6', source: 't3', target: 'c4', animated: true, style: { stroke: '#f43f5e', strokeWidth: 2 } },
  { id: 'e7', source: 'c3', target: 't4', animated: false, style: { stroke: '#06b6d4', strokeWidth: 1.5 } },
  { id: 'e8', source: 't4', target: 'c4', animated: true, style: { stroke: '#f43f5e', strokeWidth: 2 } },
  { id: 'e9', source: 'c4', target: 'c1', animated: true, style: { stroke: '#f43f5e', strokeWidth: 2.5 }, label: 'CIRCULAR', labelStyle: { fill: '#f43f5e', fontSize: 10, fontWeight: 700 } },
  { id: 'e10', source: 'c2', target: 'd1', animated: false, style: { stroke: '#a855f7', strokeWidth: 1.5, strokeDasharray: '5,5' } },
  { id: 'e11', source: 'c4', target: 'd1', animated: false, style: { stroke: '#a855f7', strokeWidth: 1.5, strokeDasharray: '5,5' }, label: 'SHARED', labelStyle: { fill: '#a855f7', fontSize: 10 } },
  { id: 'e12', source: 'c3', target: 'ip1', animated: false, style: { stroke: '#f59e0b', strokeWidth: 1.5, strokeDasharray: '5,5' } },
  { id: 'e13', source: 'c4', target: 'ip1', animated: false, style: { stroke: '#f59e0b', strokeWidth: 1.5, strokeDasharray: '5,5' } },
  { id: 'e14', source: 'c4', target: 'c5', animated: false, style: { stroke: '#64748b', strokeWidth: 1 } },
]

function NetworkGraph() {
  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes)
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges)

  return (
    <div className="fade-in">
      <h1 className="page-title">Entity Network Graph</h1>
      <div className="card" style={{ height: 'calc(100vh - 180px)', padding: 0, overflow: 'hidden' }}>
        <ReactFlow
          nodes={nodes}
          edges={edges}
          onNodesChange={onNodesChange}
          onEdgesChange={onEdgesChange}
          nodeTypes={nodeTypes}
          fitView
          attributionPosition="bottom-left"
          style={{ background: 'var(--bg-primary)' }}
        >
          <Background color="rgba(255,255,255,0.03)" gap={20} />
          <Controls style={{ background: 'var(--bg-secondary)', borderColor: 'var(--border-color)' }} />
          <MiniMap
            style={{ background: 'var(--bg-secondary)', border: '1px solid var(--border-color)' }}
            nodeColor={(node) => {
              if (node.data?.flagged) return '#f43f5e'
              const colors = { customer: '#6366f1', transaction: '#06b6d4', device: '#a855f7', ip: '#f59e0b' }
              return colors[node.data?.type] || '#6366f1'
            }}
          />
        </ReactFlow>
      </div>
    </div>
  )
}

export default NetworkGraph

const colors = {
  success: { bg: '#f0fdf4', border: '#67c23a', text: '#2e7d32', icon: '✓' },
  error: { bg: '#fef2f2', border: '#f56c6c', text: '#c62828', icon: '✕' },
  warning: { bg: '#fffbeb', border: '#e6a23c', text: '#e65100', icon: '!' },
  info: { bg: '#eff6ff', border: '#409eff', text: '#1565c0', icon: 'i' }
}

export function showToast(msg, type = 'info') {
  const c = colors[type] || colors.info
  const el = document.createElement('div')
  el.textContent = `${c.icon} ${msg}`
  Object.assign(el.style, {
    position: 'fixed', top: '24px', left: '50%', transform: 'translateX(-50%)',
    padding: '12px 24px', borderRadius: '8px', fontSize: '14px', fontWeight: '600',
    zIndex: '99999', boxShadow: '0 4px 16px rgba(0,0,0,0.12)',
    background: c.bg, border: `1.5px solid ${c.border}`, color: c.text,
    fontFamily: '"Microsoft YaHei", sans-serif', lineHeight: '1.5',
    animation: 'toastIn 0.3s ease-out'
  })
  document.body.appendChild(el)
  setTimeout(() => {
    el.style.opacity = '0'
    el.style.transition = 'opacity 0.3s'
    setTimeout(() => el.remove(), 300)
  }, 2500)
}

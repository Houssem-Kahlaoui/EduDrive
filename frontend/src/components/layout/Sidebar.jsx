// src/components/layout/Sidebar.jsx
import { Link, useLocation } from 'react-router-dom';
import { useAuthStore } from '../../store/authStore';
import { Icons } from '../../utils/icons';

export default function Sidebar() {
  const { user } = useAuthStore();
  const location = useLocation();

  const MENUS = {
    ADMIN: [
      { path: '/admin', label: 'Tableau de bord', icon: Icons.chk },   
      { path: '/admin/seances', label: 'Séances', icon: Icons.cal },
      { path: '/admin/utilisateurs', label: 'Utilisateurs', icon: Icons.users }, 
      { path: '/admin/forfaits', label: 'Forfaits', icon: Icons.box }, 
      { path: '/admin/vehicules', label: 'Véhicules', icon: Icons.car },
      { path: '/admin/examens', label: 'Examens', icon: Icons.cap },
    ],
  };

  const menuItems = MENUS[user?.role] || [];

  return (
    <aside style={{
      width: '250px',
      background: 'var(--soft)',
      borderRadius: '18px',
      padding: '14px',
      margin: '20px',
      height: 'fit-content'
    }}>
      <div style={{ padding: '8px 10px 14px', fontSize: '13px', color: 'var(--mut)' }}>
        {user?.role}
        <b style={{ display: 'block', color: 'var(--txt)', fontSize: '15px' }}>{user?.nom}</b>
      </div>

      <nav style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
        {menuItems.map((item) => {
          const isActive = location.pathname === item.path;
          return (
            <Link
              key={item.path}
              to={item.path}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '10px',
                padding: '10px 12px',
                borderRadius: '10px',
                textDecoration: 'none',
                fontSize: '14px',
                fontWeight: '500',
                background: isActive ? 'var(--pri)' : 'transparent',
                color: isActive ? 'white' : 'var(--txt)',
                transition: 'all 0.2s',
              }}
            >
              {item.icon}
              {item.label}
            </Link>
          );
        })}
      </nav>
    </aside>
  );
}
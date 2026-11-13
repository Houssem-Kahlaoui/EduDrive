// src/pages/admin/AdminDashboard.jsx
import { useAuthStore } from '../../store/authStore';

export default function AdminDashboard() {
  const { user, logout } = useAuthStore();

  return (
    <div className="w sec">
      <h2>Tableau de bord ADMIN</h2>
      <p>Bienvenue {user?.prenom} {user?.nom} !</p>
      <p style={{ color: '#666', fontSize: '14px' }}>
        Connecté en tant que : <strong>{user?.email}</strong> ({user?.role})
      </p>
      <button className="btn r" onClick={logout} style={{ marginTop: '16px' }}>
        Déconnexion
      </button>
    </div>
  );
}
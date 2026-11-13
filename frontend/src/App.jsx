import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import MainLayout from './components/layout/MainLayout';
import DashboardLayout from './components/layout/DashboardLayout';
import ProtectedRoute from './components/ProtectedRoute';
import HomePage from './pages/public/HomePage';
import LoginPage from './pages/public/LoginPage';

// Admin
import AdminDashboard from './pages/admin/AdminDashboard';

// Moniteur (temp )
import MoniteurDashboard from './pages/moniteur/MoniteurDashboard';

// Candidat (temp
import CandidatDashboard from './pages/candidat/CandidatDashboard';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public Routes*/}
        <Route element={<MainLayout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
        </Route>

        {/* Admin */}
        <Route element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
          <Route element={<DashboardLayout />}>
            <Route path="/admin" element={<AdminDashboard />} />
          </Route>
        </Route>

        {/* Moniteur */}
        <Route element={<ProtectedRoute allowedRoles={['MONITEUR']} />}>
          <Route element={<DashboardLayout />}>
            <Route path="/moniteur" element={<MoniteurDashboard />} />
          </Route>
        </Route>

        {/* Candidat */}
        <Route element={<ProtectedRoute allowedRoles={['CANDIDAT']} />}>
          <Route element={<DashboardLayout />}>
            <Route path="/candidat" element={<CandidatDashboard />} />
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
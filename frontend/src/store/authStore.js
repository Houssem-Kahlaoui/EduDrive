import { create } from 'zustand';
import { authService } from '../services/authService';

export const useAuthStore = create((set) => ({
  user: JSON.parse(localStorage.getItem('eduDriveUser')) || null,
  loading: false,
  error: null,

  login: async (email, password) => {
    set({ loading: true, error: null });
    try {
      const userData = await authService.login(email, password);
      
      localStorage.setItem('eduDriveUser', JSON.stringify(userData));
      
      set({ user: userData, loading: false });
      return { success: true, role: userData.role };
    } catch (err) {
      const message = err.response?.data?.erreur || 'Erreur de connexion. Vérifiez vos identifiants.';
      set({ error: message, loading: false });
      return { success: false, message };
    }
  },

  logout: () => {
    authService.logout();
    set({ user: null, error: null });
  }
}));
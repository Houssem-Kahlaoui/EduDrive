import api from './api';

export const authService = {
  login: async (email, password) => {
    const response = await api.post('/login', { email, password });
    return response.data; 
  },
  
  logout: () => {
    localStorage.removeItem('eduDriveUser');
  }
};
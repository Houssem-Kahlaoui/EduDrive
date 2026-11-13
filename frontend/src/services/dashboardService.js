import api from './api';

export const dashboardService = {
  getSeances: async () => {
    const response = await api.get('/admin/seances');
    return response.data;
  },
  
  getCandidats: async () => {
    const response = await api.get('/admin/candidats');
    return response.data;
  },

  getMoniteurs: async () => {
    const response = await api.get('/admin/moniteurs');
    return response.data;
  },

  getVehicules: async () => {
    const response = await api.get('/admin/vehicules');
    return response.data;
  }
};
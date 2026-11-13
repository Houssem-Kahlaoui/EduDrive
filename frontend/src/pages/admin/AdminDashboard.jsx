import { useQuery } from '@tanstack/react-query';
import { dashboardService } from '../../services/dashboardService';
import Badge from '../../components/ui/Badge';
import { Link } from 'react-router-dom';

export default function AdminDashboard() {
  const { data: seances = [], isLoading: loadSeances } = useQuery({
    queryKey: ['seances'],
    queryFn: dashboardService.getSeances,
  });

  const { data: candidats = [], isLoading: loadCandidats } = useQuery({
    queryKey: ['candidats'],
    queryFn: dashboardService.getCandidats,
  });

  const { data: moniteurs = [], isLoading: loadMoniteurs } = useQuery({
    queryKey: ['moniteurs'],
    queryFn: dashboardService.getMoniteurs,
  });

  const { data: vehicules = [], isLoading: loadVehicules } = useQuery({
    queryKey: ['vehicules'],
    queryFn: dashboardService.getVehicules,
  });

  if (loadSeances || loadCandidats || loadMoniteurs || loadVehicules) {
    return <div style={{ textAlign: 'center', padding: '50px' }}>Chargement des données...</div>;
  }

  const nbCandidats = candidats.length;
  const nbMoniteurs = moniteurs.length;
  const nbSeancesAConfirmer = seances.filter(s => s.statut === 'PLANIFIEE').length;
  const nbVehiculesDispo = vehicules.filter(v => v.etat === 'DISPONIBLE').length;
  const nbPretsExamen = candidats.filter(c => c.statutFormation === 'PRET_EXAMEN').length;

  const getNomCandidat = (id) => {
    const c = candidats.find(c => c.id === id);
    return c ? `${c.prenom} ${c.nom}` : 'Inconnu';
  };
  
  const getNomMoniteur = (id) => {
    const m = moniteurs.find(m => m.id === id);
    return m ? `${m.prenom} ${m.nom}` : 'Inconnu';
  };

  const prochainesSeances = [...seances]
    .sort((a, b) => new Date(b.dateSeance) - new Date(a.dateSeance))
    .slice(0, 5);

  return (
    <>
      <h2 style={{ marginBottom: '20px', fontSize: '22px' }}>Tableau de bord</h2>
      
      <div className="kp">
        <div className="card"><b>{nbCandidats}</b><span>Candidats</span></div>
        <div className="card"><b>{nbMoniteurs}</b><span>Moniteurs</span></div>
        <div className="card"><b>{nbSeancesAConfirmer}</b><span>Séances à confirmer</span></div>
        <div className="card"><b>{nbVehiculesDispo}/{vehicules.length}</b><span>Véhicules disponibles</span></div>
        <div className="card"><b>{nbPretsExamen}</b><span>Prêts pour l’examen</span></div>
      </div>

      <div className="card" style={{ marginTop: '20px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
          <h3 style={{ margin: 0 }}>Prochaines séances</h3>
          <Link to="/admin/seances" className="btn o s">Voir tout</Link>
        </div>
        
        <div className="tw">
          <table>
            <thead>
              <tr>
                <th>Date</th>
                <th>Heure</th>
                <th>Type</th>
                <th>Candidat</th>
                <th>Moniteur</th>
                <th>Statut</th>
              </tr>
            </thead>
            <tbody>
              {prochainesSeances.length > 0 ? (
                prochainesSeances.map((s) => (
                  <tr key={s.id}>
                    <td>{s.dateSeance}</td>
                    <td>{s.heureDebut ? s.heureDebut.substring(0, 5) : ''}</td>
                    <td>{s.typeSeance}</td>
                    <td>{getNomCandidat(s.candidatId)}</td>
                    <td>{getNomMoniteur(s.moniteurId)}</td>
                    <td><Badge status={s.statut} /></td>
                  </tr>
                ))
              ) : (
                <tr><td colSpan="6" style={{ textAlign: 'center' }}>Aucune séance planifiée</td></tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
}
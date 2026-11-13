// src/pages/admin/AdminDashboard.jsx
import Badge from '../../components/ui/Badge'; 

export default function AdminDashboard() {
  const MOCK_KPIS = [
    { value: '3', label: 'Candidats' },
    { value: '3', label: 'Moniteurs' },
    { value: '2', label: 'Séances à confirmer' },
    { value: '2/3', label: 'Véhicules disponibles' },
    { value: '1', label: 'Prêts pour l’examen' },
  ];

  const MOCK_SEANCES = [
    { date: '2026-11-10', heure: '16:00', type: 'CODE', candidat: 'Aymen Hammami', moniteur: 'Mouna Trabelsi', statut: 'TERMINEE' },
    { date: '2026-11-12', heure: '09:00', type: 'CONDUITE', candidat: 'Yassine Mansouri', moniteur: 'Sami Ben Ali', statut: 'CONFIRMEE' },
    { date: '2026-11-13', heure: '14:00', type: 'CODE', candidat: 'Yassine Mansouri', moniteur: 'Mouna Trabelsi', statut: 'PLANIFIEE' },
    { date: '2026-11-14', heure: '10:00', type: 'CONDUITE', candidat: 'Amira Jlassi', moniteur: 'Sami Ben Ali', statut: 'PLANIFIEE' },
  ];

  return (
    <>
      <h2 style={{ marginBottom: '20px', fontSize: '22px' }}>Tableau de bord</h2>
      
      <div className="kp">
        {MOCK_KPIS.map((kpi, index) => (
          <div className="card" key={index}>
            <b>{kpi.value}</b>
            <span>{kpi.label}</span>
          </div>
        ))}
      </div>

      <div className="card" style={{ marginTop: '20px' }}>
        <h3 style={{ marginBottom: '16px' }}>Prochaines séances</h3>
        
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
              {MOCK_SEANCES.map((s, i) => (
                <tr key={i}>
                  <td>{s.date}</td>
                  <td>{s.heure}</td>
                  <td>{s.type}</td>
                  <td>{s.candidat}</td>
                  <td>{s.moniteur}</td>
                  <td><Badge status={s.statut} /></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
}
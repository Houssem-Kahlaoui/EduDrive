// src/pages/public/HomePage.jsx
import { useState } from 'react';
import { Link } from 'react-router-dom';
import Button from '../../components/ui/Button';
import logo from '../../assets/logo.png'; 
import { Icons } from '../../utils/icons';

// --- DONNÉES ---
const FORFAITS = [
  { id: 1, nom: 'Pack Code', type: 'CODE_SEUL', hc: 20, hd: 0, prix: 150 },
  { id: 2, nom: 'Pack Code + Conduite', type: 'CODE_CONDUITE', hc: 20, hd: 20, prix: 690 },
  { id: 3, nom: 'Conduite à l’heure', type: 'A_L_HEURE', hc: 0, hd: 1, prix: 35 },
];

const FEATURES = [
  { icon: 'cal', title: 'Planning sans conflit', desc: 'Détection automatique des conflits d’agenda.' },
  { icon: 'clock', title: 'Suivi des heures', desc: 'Heures de code et de conduite restantes.' },
  { icon: 'chk', title: 'Évaluations', desc: 'Notes et commentaires du moniteur.' },
  { icon: 'cap', title: 'Gestion des examens', desc: 'Tentatives, résultats, statut « prêt ».' },
  { icon: 'car', title: 'Parc de véhicules', desc: 'État et disponibilité en temps réel.' },
  { icon: 'lock', title: 'Accès sécurisé', desc: 'Espaces séparés selon le rôle.' },
];

const FAQ = [
  { q: 'Qu’est-ce que la plateforme EduDrive ?', a: 'Une application web de gestion d’auto-école : inscriptions, forfaits, séances, examens et suivi des candidats.' },
  { q: 'Quelles sont les règles métier appliquées ?', a: 'Âge minimum de 18 ans, validité du code, nombre de tentatives d’examen limité et absence de conflit d’agenda.' },
  { q: 'Comment accéder à mon espace ?', a: 'Cliquez sur « Se connecter » ou choisissez directement votre profil.' },
];

export default function HomePage() {
  const [openFaq, setOpenFaq] = useState(0);

  return (
    <>
      {/* ---------- HERO ---------- */}
      <section className="hero">
        <div>
          <h1>
            Votre permis,<br />
            <em>Apprendre, Planifier & Réussir</em><br />
            avec nous.
          </h1>
          <p>
            EduDrive digitalise votre auto-école : inscription, planning des séances
            de code et de conduite, suivi des heures restantes et résultats d’examens,
            le tout en ligne.
          </p>
          <div style={{ marginTop: '20px', display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
            <a href="#forfaits">
              <Button variant="gold">Nos forfaits</Button>
            </a>
            <Link to="/login">
              <Button variant="outline">Se connecter</Button>
            </Link>
          </div>
        </div>
        <div className="ic">
          <img src={logo} alt="EduDrive Illustration" style={{ width: '250px', maxWidth: '100%' }} />
        </div>
      </section>

      {/* ---------- FORFAITS ---------- */}
      <section className="soft" id="forfaits">
        <div className="t">
          <i>Nos offres</i>
          <h2>Forfaits disponibles</h2>
        </div>
        <div className="g3">
          {FORFAITS.map((f) => (
            <div className="card" key={f.id} style={{ padding: 0, overflow: 'hidden' }}>
              <div style={{ background: 'var(--soft)', padding: '26px' }}>
                <small style={{ color: 'var(--gold)', display: 'block', fontSize: '12px', fontWeight: '500' }}>
                  {f.type.replace(/_/g, ' ')}
                </small>
                <div style={{ fontSize: '20px', fontWeight: '700', marginTop: '4px' }}>{f.nom}</div>
              </div>
              <div style={{ padding: '18px' }}>
                <ul style={{ listStyle: 'none', padding: 0, margin: '0 0 14px', color: 'var(--mut)', fontSize: '13px' }}>
                  <li>✓ {f.hc} h de code</li>
                  <li>✓ {f.hd} h de conduite</li>
                  <li>✓ Planning en ligne</li>
                  <li>✓ Suivi des heures restantes</li>
                </ul>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span style={{ fontSize: '22px', fontWeight: '700' }}>
                    {f.prix} <small style={{ fontSize: '12px' }}>DT</small>
                  </span>
                  <Link to="/login">
                    <Button size="small">En savoir plus →</Button>
                  </Link>
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* ---------- FONCTIONNALITÉS ---------- */}
      <section className="sec" id="fonctionnalites">
        <div className="t">
          <i>Débloquez le succès</i>
          <h2>Avec notre plateforme</h2>
        </div>
        <div className="g3">
          {FEATURES.map((f, i) => (
            <div className="card ft" key={i}>
              {/* L'icône dans son carré bleu clair */}
              <div className="ib">{Icons[f.icon]}</div>
              <div>
                <b>{f.title}</b>
                <span>{f.desc}</span>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* ---------- FAQ ---------- */}
      <section className="sec" id="faq">
        <div className="t">
          <i>FAQ</i>
          <h2>Questions fréquemment posées</h2>
        </div>
        <div>
          {FAQ.map((faq, i) => (
            <div key={i} style={{ borderBottom: '1px solid var(--bd)', padding: '14px 4px' }}>
              <div
                onClick={() => setOpenFaq(openFaq === i ? -1 : i)}
                style={{ fontWeight: 600, cursor: 'pointer', display: 'flex', justifyContent: 'space-between' }}
              >
                {faq.q}
                <span>{openFaq === i ? '−' : '+'}</span>
              </div>
              {openFaq === i && (
                <p style={{ color: 'var(--mut)', margin: '8px 0 0' }}>{faq.a}</p>
              )}
            </div>
          ))}
        </div>
      </section>

      {/* ---------- SECTION CONTACT ---------- */}
      <section className="ct" id="contact">
        <div className="cc">
          
          {/* Colonne de gauche : Infos */}
          <div>
            <i>Contact</i>
            <h2>Vous ne savez pas par où commencer ?</h2>
            <p className="mut">Notre équipe vous répond sous 24 h et vous aide à choisir le forfait adapté.</p>
            
            <div className="ci">
              <div className="ib">{Icons.phone}</div>
              <div>
                <span>Téléphone</span>
                <b>+216 92 248 268</b>
              </div>
            </div>

            <div className="ci">
              <div className="ib">{Icons.mail}</div>
              <div>
                <span>E-mail</span>
                <b>admin@edudrive.tn</b>
              </div>
            </div>

            <div className="ci">
              <div className="ib">{Icons.pin}</div>
              <div>
                <span>Adresse</span>
                <b>Charguia 2, Tunis</b>
              </div>
            </div>
          </div>

          {/* Colonne de droite : Formulaire */}
          <form className="fm card" onSubmit={(e) => { e.preventDefault(); alert('Message envoyé ✔'); e.target.reset(); }}>
            <div>
              <label>Nom & Prénom</label>
              <input required />
            </div>
            <div>
              <label>Téléphone</label>
              <input />
            </div>
            <div>
              <label>E-mail</label>
              <input type="email" required />
            </div>
            <div>
              <label>Message</label>
              <textarea rows="4" required></textarea>
            </div>
            {/* Bouton Envoyer centré et prenant toute la largeur */}
            <Button type="submit" style={{ width: '100%', padding: '12px', marginTop: '8px', justifyContent: 'center' }}>
              Envoyer
            </Button>
          </form>

        </div>
      </section>
    </>
  );
}
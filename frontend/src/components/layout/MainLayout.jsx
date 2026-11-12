// src/components/layout/MainLayout.jsx
import { Outlet } from 'react-router-dom';
import Header from './Header';
import Footer from './Footer';

export default function MainLayout() {
  return (
    <>
      <Header />
      <main className="w" style={{ flex: 1 }}>
        <Outlet />
      </main>
      <Footer />
    </>
  );
}
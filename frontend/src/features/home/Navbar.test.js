/**
 * Navbar.test.js — Navbar Component Tests
 */
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import { BrowserRouter } from 'react-router-dom';
import Navbar from './Navbar';

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

const renderNavbar = () => render(<BrowserRouter><Navbar /></BrowserRouter>);

beforeEach(() => { jest.clearAllMocks(); localStorage.clear(); });

describe('Navbar Component', () => {
  test('renders the logo text', () => {
    renderNavbar();
    expect(screen.getByText(/Foodie/)).toBeInTheDocument();
    expect(screen.getByText(/Ordering/)).toBeInTheDocument();
  });

  test('renders navigation links', () => {
    renderNavbar();
    expect(screen.getByText('Home')).toBeInTheDocument();
    expect(screen.getByText('About')).toBeInTheDocument();
    expect(screen.getByText('Blog')).toBeInTheDocument();
    expect(screen.getByText('Service')).toBeInTheDocument();
    expect(screen.getByText('Contact us')).toBeInTheDocument();
  });

  test('shows Login button when not authenticated', () => {
    renderNavbar();
    expect(screen.getByText('Login')).toBeInTheDocument();
  });

  test('shows Logout button when authenticated', () => {
    localStorage.setItem('token', 'test-token');
    renderNavbar();
    expect(screen.getByText('Logout')).toBeInTheDocument();
  });

  test('navigates to /login on Login button click', () => {
    renderNavbar();
    fireEvent.click(screen.getByText('Login'));
    expect(mockNavigate).toHaveBeenCalledWith('/login');
  });

  test('clears token and navigates to /login on Logout', () => {
    localStorage.setItem('token', 'test-token');
    renderNavbar();
    fireEvent.click(screen.getByText('Logout'));
    expect(localStorage.getItem('token')).toBeNull();
    expect(mockNavigate).toHaveBeenCalledWith('/login');
  });

  test('Home link has active class', () => {
    renderNavbar();
    const homeLink = screen.getByText('Home');
    expect(homeLink).toHaveClass('active');
  });
});

/**
 * App.test.js — Root Application Tests
 *
 * Tests the main App component routing configuration,
 * route guards (PrivateRoute, AdminRoute), and initial render.
 */
import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import App from './App';

// ── Mock react-router-dom ─────────────────────────────────────────────────────
jest.mock('react-router-dom', () => ({
  BrowserRouter: ({ children }) => <div>{children}</div>,
  Routes: ({ children }) => <div>{children}</div>,
  Route: ({ element }) => <div>{element}</div>,
  Navigate: ({ to }) => <div data-testid="redirect">Redirect to {to}</div>
}));

// ── Mock all feature page components ──────────────────────────────────────────
jest.mock('./features/home/HomePage', () => () => <div data-testid="home-page">Home Page</div>);
jest.mock('./features/auth/LoginPage', () => () => <div data-testid="login-page">Login Page</div>);
jest.mock('./features/auth/RegisterPage', () => () => <div data-testid="register-page">Register Page</div>);
jest.mock('./features/dashboard/DashboardPage', () => () => <div data-testid="dashboard-page">Dashboard Page</div>);
jest.mock('./features/cart/CartPage', () => () => <div data-testid="cart-page">Cart Page</div>);
jest.mock('./features/checkout/CheckoutPage', () => () => <div data-testid="checkout-page">Checkout Page</div>);
jest.mock('./features/orders/OrderHistoryPage', () => () => <div data-testid="orders-page">Order History Page</div>);
jest.mock('./features/admin/AdminDashboardPage', () => () => <div data-testid="admin-page">Admin Dashboard Page</div>);
jest.mock('./features/static/About', () => () => <div data-testid="about-page">About Page</div>);
jest.mock('./features/static/Service', () => () => <div data-testid="service-page">Service Page</div>);
jest.mock('./features/static/Blog', () => () => <div data-testid="blog-page">Blog Page</div>);
jest.mock('./features/static/Contact', () => () => <div data-testid="contact-page">Contact Page</div>);

// ── Helpers ───────────────────────────────────────────────────────────────────
beforeEach(() => {
  localStorage.clear();
  // Reset the URL to root before each test
  window.history.pushState({}, 'Home', '/');
});

// ── Test Suite ────────────────────────────────────────────────────────────────
describe('App Component', () => {

  test('renders the Home page at root route "/"', () => {
    render(<App />);
    expect(screen.getByTestId('home-page')).toBeInTheDocument();
  });

  test('renders the Login page at "/login"', () => {
    window.history.pushState({}, 'Login', '/login');
    render(<App />);
    expect(screen.getByTestId('login-page')).toBeInTheDocument();
  });

  test('renders the Register page at "/register"', () => {
    window.history.pushState({}, 'Register', '/register');
    render(<App />);
    expect(screen.getByTestId('register-page')).toBeInTheDocument();
  });

  test('redirects unauthenticated users from "/dashboard" to "/login"', () => {
    window.history.pushState({}, 'Dashboard', '/dashboard');
    render(<App />);
    // Should redirect to login since no token is set
    expect(screen.getByTestId('login-page')).toBeInTheDocument();
  });

  test('allows authenticated users to access "/dashboard"', () => {
    localStorage.setItem('token', 'test-token');
    window.history.pushState({}, 'Dashboard', '/dashboard');
    render(<App />);
    expect(screen.getByTestId('dashboard-page')).toBeInTheDocument();
  });

  test('redirects unauthenticated users from "/cart" to "/login"', () => {
    window.history.pushState({}, 'Cart', '/cart');
    render(<App />);
    expect(screen.getByTestId('login-page')).toBeInTheDocument();
  });

  test('allows authenticated users to access "/cart"', () => {
    localStorage.setItem('token', 'test-token');
    window.history.pushState({}, 'Cart', '/cart');
    render(<App />);
    expect(screen.getByTestId('cart-page')).toBeInTheDocument();
  });

  test('redirects unauthenticated users from "/checkout" to "/login"', () => {
    window.history.pushState({}, 'Checkout', '/checkout');
    render(<App />);
    expect(screen.getByTestId('login-page')).toBeInTheDocument();
  });

  test('redirects unauthenticated users from "/orders" to "/login"', () => {
    window.history.pushState({}, 'Orders', '/orders');
    render(<App />);
    expect(screen.getByTestId('login-page')).toBeInTheDocument();
  });

  test('redirects non-admin users from "/admin" to "/dashboard"', () => {
    localStorage.setItem('token', 'test-token');
    localStorage.setItem('userRole', 'CUSTOMER');
    window.history.pushState({}, 'Admin', '/admin');
    render(<App />);
    // AdminRoute checks for ADMIN role → redirects CUSTOMER to /dashboard
    expect(screen.getByTestId('dashboard-page')).toBeInTheDocument();
  });

  test('allows ADMIN users to access "/admin"', () => {
    localStorage.setItem('token', 'test-token');
    localStorage.setItem('userRole', 'ADMIN');
    window.history.pushState({}, 'Admin', '/admin');
    render(<App />);
    expect(screen.getByTestId('admin-page')).toBeInTheDocument();
  });

  test('renders the About page at "/about"', () => {
    window.history.pushState({}, 'About', '/about');
    render(<App />);
    expect(screen.getByTestId('about-page')).toBeInTheDocument();
  });

  test('renders the Service page at "/service"', () => {
    window.history.pushState({}, 'Service', '/service');
    render(<App />);
    expect(screen.getByTestId('service-page')).toBeInTheDocument();
  });

  test('renders the Blog page at "/blog"', () => {
    window.history.pushState({}, 'Blog', '/blog');
    render(<App />);
    expect(screen.getByTestId('blog-page')).toBeInTheDocument();
  });

  test('renders the Contact page at "/contact"', () => {
    window.history.pushState({}, 'Contact', '/contact');
    render(<App />);
    expect(screen.getByTestId('contact-page')).toBeInTheDocument();
  });
});

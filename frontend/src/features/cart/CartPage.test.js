/**
 * CartPage.test.js — Cart Page Tests
 */
import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import { BrowserRouter } from 'react-router-dom';
import { CartProvider } from './CartContext';
import Cart from './CartPage';

jest.mock('../../shared/api/api', () => ({
  __esModule: true,
  default: {
    get: jest.fn().mockResolvedValue({ data: { success: true, data: [] } }),
    post: jest.fn(), put: jest.fn(), delete: jest.fn(),
    interceptors: { request: { use: jest.fn() } },
  },
}));

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

const renderCart = () => render(
  <BrowserRouter>
    <CartProvider>
      <Cart />
    </CartProvider>
  </BrowserRouter>
);

beforeEach(() => { jest.clearAllMocks(); localStorage.clear(); });

describe('CartPage Component', () => {
  test('renders "My Order" heading', () => {
    renderCart();
    expect(screen.getByText('My Order')).toBeInTheDocument();
  });

  test('shows empty cart message when no items', () => {
    renderCart();
    expect(screen.getByText(/Your cart is empty/i)).toBeInTheDocument();
  });

  test('renders Browse Food button in empty state', () => {
    renderCart();
    expect(screen.getByText('Browse Food')).toBeInTheDocument();
  });
});

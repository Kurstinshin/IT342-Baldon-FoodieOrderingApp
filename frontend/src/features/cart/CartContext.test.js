/**
 * CartContext.test.js — Cart Context Tests
 */
import React from 'react';
import { render, screen, act, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import '@testing-library/jest-dom';
import { CartProvider, useCart } from './CartContext';

jest.mock('../../shared/api/api', () => ({
  __esModule: true,
  default: {
    get: jest.fn(), post: jest.fn(), put: jest.fn(), delete: jest.fn(),
    interceptors: { request: { use: jest.fn() } },
  },
}));

import API from '../../shared/api/api';

function TestConsumer() {
  const { cart, addToCart, clearCart } = useCart();
  return (
    <div>
      <span data-testid="count">{cart.length}</span>
      <button data-testid="add" onClick={() => addToCart({ id: 1, name: 'Burger', price: 100, img: 'b.jpg' })}>Add</button>
      <button data-testid="clear" onClick={() => clearCart()}>Clear</button>
    </div>
  );
}

beforeEach(() => {
  jest.clearAllMocks();
  localStorage.clear();
  API.get.mockResolvedValue({ data: { success: true, data: [] } });
  API.post.mockResolvedValue({ data: { success: true } });
  API.delete.mockResolvedValue({ data: { success: true } });
});

describe('CartContext', () => {
  test('initializes empty', () => {
    render(<CartProvider><TestConsumer /></CartProvider>);
    expect(screen.getByTestId('count')).toHaveTextContent('0');
  });

  test('adds item optimistically', async () => {
    localStorage.setItem('userId', '1');
    API.get.mockResolvedValueOnce({ data: { success: true, data: [] } }) // initial fetch
           .mockResolvedValueOnce({ data: { success: true, data: [{ id: 101, food: { id: 1, name: 'Burger', price: 100, img: 'b.jpg' }, quantity: 1 }] } }); // after add

    render(<CartProvider><TestConsumer /></CartProvider>);
    await act(async () => { await userEvent.click(screen.getByTestId('add')); });
    await waitFor(() => expect(screen.getByTestId('count')).toHaveTextContent('1'));
  });

  test('calls API.post on add', async () => {
    localStorage.setItem('userId', '1');
    render(<CartProvider><TestConsumer /></CartProvider>);
    await act(async () => { await userEvent.click(screen.getByTestId('add')); });
    await waitFor(() => expect(API.post).toHaveBeenCalledWith('/cart/1', { foodId: 1, quantity: 1 }));
  });

  test('clears cart', async () => {
    localStorage.setItem('userId', '1');
    render(<CartProvider><TestConsumer /></CartProvider>);
    await act(async () => { await userEvent.click(screen.getByTestId('add')); });
    await act(async () => { await userEvent.click(screen.getByTestId('clear')); });
    await waitFor(() => expect(screen.getByTestId('count')).toHaveTextContent('0'));
  });

  test('skips API if no userId', async () => {
    render(<CartProvider><TestConsumer /></CartProvider>);
    await act(async () => { await userEvent.click(screen.getByTestId('add')); });
    expect(API.post).not.toHaveBeenCalled();
  });

  test('fetches cart on mount', async () => {
    localStorage.setItem('userId', '42');
    render(<CartProvider><TestConsumer /></CartProvider>);
    await waitFor(() => expect(API.get).toHaveBeenCalledWith('/cart/42'));
  });
});

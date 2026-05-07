import Navbar from "./Navbar";
import { useNavigate } from "react-router-dom";
import "./home.css";

function Home() {
  const navigate = useNavigate();

  return (
    <div className="home">

      <Navbar />

      {/* HERO */}
      <div className="hero">

        {/* IMAGE LEFT */}
        <div className="hero-image">
          <img
            src="homeimage.jpeg"
            alt="food"
          />
        </div>

        {/* TEXT RIGHT */}
        <div className="hero-text">
          <h1>
            Happy With <span>Delicious <br />Food  </span>
            And  Order <br />Variety Of Foods
          </h1>

          <p>
            Exploring new food with different kinds that you can try at this <br />
            place and get a good price from us as well. We will make a good <br />
            impact to our customers.
          </p>

          <div className="buttons">
            <button
              className="order-btn"
              onClick={() => navigate("/dashboard")}
            >
              Order Food <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" style={{ marginLeft: "6px" }}><circle cx="9" cy="21" r="1"></circle><circle cx="20" cy="21" r="1"></circle><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path></svg>
            </button>

            <button className="learn-btn">
              Learn More
            </button>
          </div>
        </div>

      </div>

      {/* HOW TO ORDER */}
      <div className="how-order">
        <h2>How You Can Order</h2>


      </div>

    </div>
  );
}

export default Home;

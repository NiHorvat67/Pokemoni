import { useState } from "react";
import { useGSAP } from "@gsap/react";
import gsap from "gsap";
import ProductCard from "./ProductCard";


const AdvertisementsGrid = ({ products }: { products: any[] }) => {

  const increment = 5
  const initialSize = 5
  const [numCards, setNumCards] = useState(initialSize)



  useGSAP(() => {
    const cards = gsap.utils.toArray(".product-card").slice(numCards - increment < initialSize ? 0 : numCards - increment)
    gsap.fromTo(cards, {
      opacity: 0,
      ease: "power1.inOut",
      y: 25,
      stagger: .06,
      duration: .5,
    }, { opacity: 1, y: 0 })
  }, [products, numCards])


  useGSAP(() => {
    const timeout = setTimeout(() => {
      const cards = gsap.utils.toArray(".product-card").slice(
        numCards - increment < initialSize ? 0 : numCards - increment
      );
      gsap.fromTo(cards, {
        opacity: 0,
        ease: "power1.inOut",
        y: 25,
        stagger: 0.06,
        duration: 0.5,
      }, { opacity: 1, y: 0 });
    }, 100); // or 50 ms if needed

    return () => clearTimeout(timeout);
  }, [])

  return (
    <>
      <section id="products-grid" className="grid min-[1250px]:grid-cols-4 min-[1000px]:grid-cols-3 min-[700px]:grid-cols-2 grid-cols-1 max-sm:justify-items-center gap-11">
        {products.map((product: any, index) => {
          if (index < numCards)
            return (
              <div key={product.advertisementId} className="product-card opacity-0">
                <ProductCard
                  category={product.itemType.itemtypeName}
                  img={product.itemImagePath}
                  productName={product.itemName}
                  owner={{ name: `${product.trader.userFirstName} ${product.trader.userLastName}`, id: product.trader.accountId }}
                  desc={product.itemDescription}
                  price={product.advertisementPrice}
                  advertisementId={product.advertisementId}
                ></ProductCard>
              </div>
            )
        })}
      </section>
      {numCards < products.length &&
        <div className="flex justify-center mt-20">
          <button
            onClick={() => { setNumCards(numCards + increment) }}
            className="cursor-pointer text-primary bg-[#102B19] font-inter text-[16px] rounded-[8px] px-4 py-1.5">
            Show more
          </button>
        </div>
      }
    </>
  );
}

export default AdvertisementsGrid;

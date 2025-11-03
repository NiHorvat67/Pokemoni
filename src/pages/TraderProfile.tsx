import profileImg from "../assets/images/profile.jpeg"
import { locationIcon, mailIcon, starIcon, phoneIcon } from "../assets/icons"
import "../styles/TraderProfile.css"
import ProductCard from "../components/ProductCard"
import { products as fakeProducts } from "@/constants"

import gsap from 'gsap';
import { useGSAP } from '@gsap/react';
import { useQuery } from "@tanstack/react-query"
import axios from 'axios';
import { useParams } from "react-router-dom"
import { useState, useEffect } from "react"
import { useMutation } from "@tanstack/react-query"

const TraderProfile = () => {
  const { userId } = useParams()

  const increment = 5
  const initialSize = 5
  const [numCards, setNumCards] = useState(initialSize)
  const [products, setProducts] = useState([])


  const { status, error, data } = useQuery({
    queryKey: ["traderAccount"],
    queryFn: async () => {
      return axios
        .get(`/api/accounts/${userId}`)
        .then(res => {
          if (res.data == "") {
            window.location.pathname = "/error"
          }
          return res.data
        })
        .catch(err => console.log(err))
    }
  })


  useEffect(() => {
    mutate()
  }, [])

  const { mutate } = useMutation({
    mutationFn: async () => {
      return axios
        .get(`/api/advertisements/account/${userId}`)
        .then(res => {
          // console.log(res.data)
          return res.data
        })
        .catch(err => console.log(err))
    },
    onSuccess: queryResult => {
      setProducts(queryResult)
    }

  })

  const infoItems = [
    { icon: mailIcon, text: data?.userEmail },
    { icon: locationIcon, text: data?.userLocation },
    { icon: phoneIcon, text: data?.userContact },
  ]
  if (data?.accountRating) infoItems.push({ icon: starIcon, text: `${data?.accountRating}/5` })


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
    <section className="padding-x padding-t pb-20">
      <section className="max-container">

        <section id="info-container" className="mb-19 sm:mb-34">
          <img
            id="profile-image"
            src={profileImg}
            alt="profile"
            className="sm:ml-[-10px] ml-[-4px] rounded-full h-[152px] w-[152px] sm:h-[206px] sm:w-[206px] object-cover"
          />
          <div id="name">
            <h1 className="text-white text-2xl sm:text-[32px] font-medium mb-1">{data?.userFirstName} {data?.userLastName}</h1>
            <p className="font-inter text-desc capitalize">{data?.accountRole}</p>
          </div>

          <div id="desc" className="flex gap-4 flex-col">
            {infoItems.map((item) => (
              <div key={item.text} className="text-white font-inter flex items-start gap-3">
                <img src={item.icon} alt="list item icon" />
                {item.text}
              </div>
            ))}
          </div>


        </section>


        {/* <section className="flex gap-11 flex-wrap"> */}
        {data?.accountRole == "trader" &&
          <>
            <h1 className="text-white font-inter font-medium text-xl sm:text-2xl mb-[54px]">{data?.userFirstName} {data?.userLastName} is renting</h1>
            <section className="grid min-[1250px]:grid-cols-4 min-[1000px]:grid-cols-3 min-[700px]:grid-cols-2 grid-cols-1 max-sm:justify-items-center gap-11">
              {products.map((product: any, index) => {
                if (index < numCards)
                  return (
                    <div key={index} className="product-card opacity-0">
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
        }

      </section>
    </section >
  );
}


export default TraderProfile;

import profileImg from "../assets/images/profile.jpeg"
import { locationIcon, mailIcon, starIcon, phoneIcon } from "../assets/icons"
import "../styles/TraderProfile.css"

import AdvertisementsGrid from "@/components/AdvertisementsGrid"
import { useQuery } from "@tanstack/react-query"
import axios from 'axios';
import { useParams } from "react-router-dom"
import { useState, useEffect } from "react"
import { useMutation } from "@tanstack/react-query"

const TraderProfile = () => {
  const { userId } = useParams()

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

        {data?.accountRole == "trader" &&
          <>
            <h1 className="text-white font-inter font-medium text-xl sm:text-2xl mb-[54px]">{data?.userFirstName} {data?.userLastName} is renting</h1>
            <AdvertisementsGrid products={products} />
          </>
        }

      </section>
    </section >
  );
}


export default TraderProfile;

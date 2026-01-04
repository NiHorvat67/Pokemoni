import profileImg from "../assets/images/profile.jpeg"
import { locationIcon, mailIcon, starIcon, phoneIcon } from "../assets/icons"
import "../styles/Profile.css"

import AdvertisementsGrid from "@/components/AdvertisementsGrid"
import { useQuery } from "@tanstack/react-query"
import axios from 'axios';
import { useParams } from "react-router-dom"
import { useState, useEffect } from "react"
import { useMutation } from "@tanstack/react-query"
import { reservations } from "@/constants"
import { DropdownMenuDialog } from "@/components/DropdownMenuDialog"
import { MoreHorizontalIcon } from "lucide-react"
import { Button } from "@/components/ui/button"

const Profile = () => {
  const { userId } = useParams()

  const [products, setProducts] = useState([])


  const { data } = useQuery({
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

        <section id="info-container" className="mb-19 sm:mb-34 max-sm:!gap-x-[0px]">
          <img
            id="profile-image"
            src={profileImg}
            alt="profile"
            className="sm:ml-[-10px] ml-[-4px] rounded-full h-[110px] w-[110px] sm:h-[206px] sm:w-[206px] object-cover"
          />
          <div id="name" className="max-sm:ml-[-20px] ">
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
            <h1 className="text-white font-inter font-medium text-xl sm:text-2xl mb-[54px]">{data?.userFirstName} {data?.userLastName} {products.length === 0 ? "is not renting any products" : "is renting"}</h1>
            <AdvertisementsGrid products={products} />
          </>
        }

        <div>
          <h1 className="text-white font-inter font-medium text-xl sm:text-2xl mb-[44px] mt-[54px]">Your reservations</h1>

          <div className="md:hidden flex flex-row flex-wrap gap-x-4 gap-y-4">
            {reservations.map((reservation, index) => (

              <div className="bg-[#222423] px-6.5 py-3.5 rounded-[8px] text-white min-w-[6rem] w-full">

                <div className="flex items-center justify-between mb-4">
                  <a href={`/advertisement/${1}`} className="text-white font-medium text-base">{reservation.itemName}</a>
                  {reservation.status === "inactive" &&
                    <div className="text-white text-base">
                      <DropdownMenuDialog />
                    </div>
                  }
                  {reservation.status === "active" &&
                    <div className="opacity-0">
                      <Button variant="transparent" className="hover:bg-[#444945] cursor-pointer" aria-label="Open menu" size="icon-sm">
                        <MoreHorizontalIcon />
                      </Button>
                    </div>
                  }
                </div>

                <div className="grid grid-cols-2 border-b-[1px] border-[#3A403D] py-[8px]">
                  <h3>Status</h3>
                  <h3>{reservation.status}</h3>
                </div>
                <div className="grid grid-cols-2 border-b-[1px] border-[#3A403D] py-[8px]">
                  <h3>Trader</h3>
                  <a href={`/profile/${2}`}>{reservation.traderName}</a>
                </div>
                <div className="grid grid-cols-2 border-b-[1px] border-[#3A403D] py-[8px]">
                  <h3>Date Start</h3>
                  <h3>{reservation.dateStart}</h3>
                </div>
                <div className="grid grid-cols-2 py-[8px]">
                  <h3>Date End</h3>
                  <h3>{reservation.dateEnd}</h3>
                </div>

              </div>
            ))}
          </div>

          <table className="max-md:hidden font-inter text-white border-separate border-spacing-x-0 border-spacing-y-2 w-full text-nowrap">
            <tr className="">
              <th className="text-left pl-6 pr-2 font-normal">Product</th>
              <th className="text-left px-2 font-normal">Trader</th>
              <th className="text-left px-2 font-normal">Status</th>
              <th className="text-left px-2 font-normal">Date</th>
            </tr>
            {/* provjeriti jeli trenutni korisnik i vlasnik profila */}
            {reservations.map((reservation, index) => (
              <tr className="">
                <td className="bg-[#222423] pl-6 pr-2 py-2.5 rounded-l-[8px]">
                  <a href={`/advertisement/${1}`} className="text-white font-medium text-base">{reservation.itemName}</a>
                </td>

                <td className="bg-[#222423] px-2">
                  <a href={`/profile/${2}`} className="text-white text-base">{reservation.traderName}</a>
                </td>

                <td className="bg-[#222423] px-2">
                  <h3 className="text-desc text-base">{reservation.status}</h3>
                </td>

                <td className="bg-[#222423] px-2">
                  <h3 className="text-desc text-base">{reservation.dateStart} - {reservation.dateEnd}</h3>
                </td>

                <td className="bg-[#222423] px-2 rounded-r-[8px]">
                  {reservation.status === "inactive" &&
                    <div className="justify-self-end">
                      <div className="text-white text-base">
                        <DropdownMenuDialog />
                      </div>
                    </div>
                  }
                  {reservation.status === "active" &&
                    <div className="justify-self-end opacity-0">
                      <div>
                        <Button variant="transparent" className="hover:bg-[#444945] cursor-pointer" aria-label="Open menu" size="icon-sm">
                          <MoreHorizontalIcon />
                        </Button>
                      </div>
                    </div>
                  }
                </td>
              </tr>
            ))}
          </table>

        </div>

      </section>
    </section >
  );
}


export default Profile;

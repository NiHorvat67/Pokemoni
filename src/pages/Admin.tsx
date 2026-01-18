import Button from "@/components/Button";
import Input from "@/components/Input";
import { useState } from "react";
import { dateRangeIcon } from "@/assets/icons";
import { useQuery, useMutation } from "@tanstack/react-query";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Admin = () => {

  const [price, setPrice] = useState<number | "">("")
  const navigate = useNavigate()

  const { data: subscriptionPrice } = useQuery({
    queryKey: ["subscriptionPrice"],
    queryFn: async () => {
      return axios
        .get(`/api/subscription-price/`)
        .then(res => {
          if (res.data == "") {
            navigate("/error")
          }
          return res.data
        })
        .catch(err => {
          console.log(err)
          if (err.response.status === 400) {
            navigate("/error")
          }
        })
    }
  })

  const { data: reports } = useQuery({
    queryKey: ["reports"],
    queryFn: async () => {
      return axios
        .get(`/api/reports/with-accounts/`)
        .then(res => {
          return res.data
        })
        .catch(err => {
          console.log(err)
          if (err.response.status === 400) {
            navigate("/error")
          }
        })
    }
  })

  const { mutate: updateSuscriptionPrice } = useMutation({
    mutationFn: async () => {
      return axios({
        method: "POST",
        url: "/api/subscription-price/set",
        data: { price: Number(price) * 100 }
        ,
      })
        .then(res => {
          return res.data
        })
        .catch(err => console.log(err))
    },
    onSuccess: () => {
      window.location.reload()
    }
  })

  const { mutate: deleteAccount } = useMutation({
    mutationFn: async (userId) => {
      return axios({
        method: "GET",
        url: `/api/accounts/delete/${userId}`,
      })
        .then(res => {
          return res.data
        })
        .catch(err => console.log(err))
    },
    onSuccess: () => {
      window.location.reload()
    }
  })



  return (
    <section className="padding-x padding-t">
      <div className="font-inter max-container flex flex-col gap-18 sm:gap-20">

        <h1 className="font-medium text-2xl text-white">Admin panel</h1>

        <section className="">
          <h2 className="text-white text-xl font-medium mb-3">Yearly subscription — {subscriptionPrice / 100}€</h2>
          <p className="text-desc mb-7">Set the price of the yearly subscription</p>
          <form className="flex gap-4 sm:gap-7" onSubmit={() => { updateSuscriptionPrice() }}>
            <Input type="number" placeholder="Price" state={price} setState={setPrice} />
            <Button submit={true} text="Update" icon={false} long={false} onClick={() => { }} />
          </form>

        </section>

        <section>
          <h2 className="text-white text-xl font-medium mb-3">Reports</h2>
          <p className="text-desc mb-7">Displaying all customer-related reports from sellers.</p>

          <div className="flex flex-col gap-8 mb-32">
            {reports?.map((reportData: any) => (
              <div key={reportData.report.report_id} className="flex gap-6 flex-col bg-[#222423] rounded-[8px] sm:px-7 py-4 px-5 sm:py-5 sm:px-9 sm:py-7 max-w-[670px]">
                <div className="flex flex-wrap gap-5 justify-between text-white">
                  <h2 className="">
                    <a href={`/profile/${reportData.reporter.accountId}`} className="font-bold">{reportData.reporter.userFirstName} {reportData.reporter.userLastName} </a>
                    reported
                    <a href={`/profile/${reportData.reported.accountId}`} className="font-bold"> {reportData.reported.userFirstName} {reportData.reported.userLastName}</a>
                  </h2>
                  <div className="flex items-center gap-2 font-medium">
                    <img src={dateRangeIcon} alt="date icon" />
                    {new Date(reportData.report.created_at).toLocaleDateString("hr-HR")}
                  </div>
                </div>

                <p className="text-desc">
                  {reportData.report.report_details}
                </p>

                <div className="flex gap-5 sm:gap-7 flex-wrap">
                  <Button icon={false} text="Delete account" className="!bg-[#E5002E] hover:shadow-[0_0_15px_#E5002E] text-white !py-2 !px-4" long={false} onClick={() => { deleteAccount(reportData.reported.accountId) }} />
                </div>
              </div>
            )
            )}

          </div>
        </section>


      </div>
    </section>
  );
}

export default Admin;

import { useState } from "react"
import { MoreHorizontalIcon } from "lucide-react"

import { Button } from "@/components/ui/button"
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { Field, FieldGroup, FieldLabel } from "@/components/ui/field"
import { Textarea } from "@/components/ui/textarea"
import RateService from "./RateService"
import { useMutation } from "@tanstack/react-query"
import axios from "axios"

export function DropdownMenuDialog({ reporterId, reportedId }: { reporterId: number, reportedId: number }) {
  const [showRateDialog, setShowRateDialog] = useState(false)
  const [showReportDialog, setShowReportDialog] = useState(false)

  const [rating, setRating] = useState(null)
  const [reportDetails, setReportDetails] = useState("")

  const { mutate: sendReport } = useMutation({
    mutationFn: async () => {
      return axios({
        method: "POST",
        url: "/api/reports/create",
        data: {
          "reporter_id": reporterId,
          "reported_id": reportedId,
          "report_details": reportDetails,
        }
        ,
      })
        .then(res => {
          return res.data
        })
        .catch(err => console.log(err))
    },
    onSuccess: queryResult => {
      window.location.reload()
    }
  })

  const submitRating = () => {
    console.log("rating")
  }
  const submitReport = () => {
    console.log("jdklsajdklsaj")
    sendReport()
  }

  return (
    <>
      <DropdownMenu modal={false}>
        <DropdownMenuTrigger asChild>
          <Button variant="transparent" className="hover:bg-[#444945] cursor-pointer" aria-label="Open menu" size="icon-sm">
            <MoreHorizontalIcon />
          </Button>
        </DropdownMenuTrigger>

        <DropdownMenuContent className="w-10 bg-input-bg" align="end">
          <DropdownMenuGroup>
            <DropdownMenuItem className="focus:bg-primary/7 transition duration-100 ease-in-out" onSelect={() => setShowRateDialog(true)}>
              Rate
            </DropdownMenuItem>
            <DropdownMenuItem className="focus:bg-primary/7 transition duration-100 ease-in-out" onSelect={() => setShowReportDialog(true)}>
              Report
            </DropdownMenuItem>
          </DropdownMenuGroup>
        </DropdownMenuContent>

      </DropdownMenu>

      <Dialog open={showRateDialog} onOpenChange={setShowRateDialog}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Rate service</DialogTitle>
            <DialogDescription>
              Your rental period is over—share your experience with the equipment.
            </DialogDescription>
          </DialogHeader>
          <RateService rating={rating} setRating={setRating} />

          <DialogFooter>
            <DialogClose asChild>
              <Button variant="outline">Cancel</Button>
            </DialogClose>
            <Button type="submit" className="hover:bg-primary" onClick={submitRating}>Confirm</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <Dialog open={showReportDialog} onOpenChange={setShowReportDialog}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Report trader</DialogTitle>
            <DialogDescription>
              Let us know about any issues or concerns you had with the trader.
            </DialogDescription>
          </DialogHeader>
          <FieldGroup className="py-3">
            <Field>
              <FieldLabel htmlFor="report-description">Tell us what happened</FieldLabel>
              <Textarea
                id="report-description"
                name="report-description"
                placeholder=""
                value={reportDetails}
                onChange={(e) => { setReportDetails(e.target.value) }}
              />
            </Field>
          </FieldGroup>
          <DialogFooter>
            <DialogClose asChild>
              <Button variant="outline">Cancel</Button>
            </DialogClose>
            <Button type="submit" className="hover:bg-primary" onClick={submitReport}>Confirm</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  )
}

import React, { useState, useEffect } from "react";
import { Button } from "react-bootstrap";
import Api, { authApis, endpoints } from "../configs/Api";
import { toast } from "react-toastify";

const FollowButton = ({ employeeId, employerId, onChange }) => {
  const [isFollowing, setIsFollowing] = useState(false);
  const [isHovering, setIsHovering] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const checkFollow = async () => {
      try {
        const res = await authApis.get(endpoints["followExists"](employeeId, employerId));
        setIsFollowing(res.data.exists);
      } catch (err) {
        console.error("Lỗi khi kiểm tra follow:", err);
      }
    };

    if (employeeId && employerId) {
      checkFollow();
    }
  }, [employeeId, employerId]);

  const handleFollow = async () => {
    try {
      setLoading(true);
      const params = new URLSearchParams();
      params.append("employee", employeeId);
      params.append("employer", employerId);

      await authApis().post(endpoints["follow"], params, {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        }
      });

      toast.success("✅ Bạn đã theo dõi công ty.");
      setIsFollowing(true);
      onChange && onChange(true); // notify parent
    } catch (err) {
      console.error("❌ Lỗi khi follow:", err);
      toast.error("Theo dõi thất bại!");
    } finally {
      setLoading(false);
    }
  };

  const handleUnfollow = async () => {
    try {
      setLoading(true);
      await authApis().delete(endpoints["unfollow"](employeeId, employerId));
      toast.info("❎ Bạn đã bỏ theo dõi.");
      setIsFollowing(false);
      onChange && onChange(false);
    } catch (err) {
      console.error("❌ Lỗi khi unfollow:", err);
      toast.error("Bỏ theo dõi thất bại!");
    } finally {
      setLoading(false);
    }
  };

  const handleClick = () => {
    if (isFollowing) handleUnfollow();
    else handleFollow();
  };

  return (
    <Button
      variant={isFollowing ? (isHovering ? "danger" : "secondary") : "primary"}
      className="follow-button-fixed w-100 py-2"
      onClick={handleClick}
      onMouseEnter={() => setIsHovering(true)}
      onMouseLeave={() => setIsHovering(false)}
      disabled={loading}
    >
      {isFollowing
        ? (isHovering ? "Bỏ theo dõi" : "Đã theo dõi")
        : "Follow"}
    </Button>
  );
};

export default FollowButton;

package com.petterp.floatingx.app.issues

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.petterp.floatingx.app.R
import com.petterp.floatingx.app.simple.FxAnimationImpl
import com.petterp.floatingx.assist.Direction
import com.petterp.floatingx.util.viewToFx

/**
 * view的浮窗设置点击事件后一点击Fragment其他的点击事件会失效
 * https://github.com/Petterpx/FloatingX/issues/28
 * @author petterp
 */
class Issues28Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            FrameLayout(this).apply {
                id = R.id.scope_activity_id
            }
        )
        supportFragmentManager.beginTransaction().add(R.id.scope_activity_id, Issues28Fragment())
            .commit()
    }
}

class Issues28Fragment : Fragment() {

    private val rootView by lazy {
        FrameLayout(requireContext())
    }
    private val scopeView by lazy {
        FrameLayout(requireContext()).apply {
            id = R.id.scope_activity_view_id
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ).apply {
                topMargin = 400
            }
            setBackgroundColor(Color.GREEN)
        }
    }

    private val btnFloatingClick by lazy {
        Button(this.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            text = "设置悬浮窗点击事件"
            setOnClickListener {
                scopeFloating.setClickListener {
                    Toast.makeText(context, "点击了悬浮窗", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val btnFragment by lazy {
        Button(this.context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 200
            }
            text = "fragment-子button点击响应测试"
            setOnClickListener {
                Toast.makeText(context, "点击了子Button", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val scopeFloating by viewToFx(R.id.scope_activity_view_id, this) {
        setLayout(R.layout.item_floating)
        setEdgeOffset(40f)
        setAnimationImpl(FxAnimationImpl())
        setGravity(Direction.RIGHT_OR_TOP)
        setEnableLog(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = rootView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView.addView(btnFloatingClick)
        rootView.addView(btnFragment)
        rootView.addView(scopeView)
        rootView.setOnClickListener {
            Toast.makeText(requireContext(), "点击了fragment-背景", Toast.LENGTH_SHORT).show()
        }
        scopeFloating.show()
    }
}
